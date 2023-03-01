from flask import Flask, session, render_template, request, url_for, redirect,g,json,jsonify
import config
from exts import db
from models import User, Docterinfo, Post, Answer, Comment, Picture, One_Order, friend_apply,User_History,Order_Comment
from sqlalchemy import or_
from method import class_to_dict,User_to_BackUser,Users_to_BackUsers
from method2 import bytelist
from backmodels import BackUser,BackHead,BackPost,Backanswer,Backcomment
from qiniu import Auth
from Strings import Default_head_url

app = Flask(__name__)
app.config.from_object(config)
db.init_app(app)



#七牛云需要,传照片时
#需要填写你的 Access Key 和 Secret Key
access_key = 'GIUfL1VLF_v5U4bvCH_6YuWSkXHMH1c3x4mxJZyK'
secret_key = 'RpF7tQhdyKl61bL-5rWs1zdyJfEOdmL5dVcgl7QC'
#构建鉴权对象
q = Auth(access_key, secret_key)
#要上传的空间
bucket_name = 'xiaoyi666'




#注册step1
#param：username password
#return：BackUser不含密码 不含docinfo_id，,错误时id=0
@app.route('/register1',methods=["POST"])
def register1():
    username = request.form.get("username")
    password = request.form.get("password")

    user = User.query.filter(User.username == username).first()
    if user == None:
        user = User(username=username, password=password,head_url=Default_head_url,isdocter=False)
        db.session.add(user)
        db.session.commit()
        user = User_to_BackUser(user)
        user = class_to_dict(user)
        return jsonify(user)
    else:
        erroruser = BackUser(0, "0", "0", "0", False)#所有user错误返回这个玩意
        return jsonify(class_to_dict(erroruser))


#注册step2
#param：id token
#return string,没必要用
@app.route('/register2',methods=["POST"])
def register2():
    id = request.form.get("id")
    token = request.form.get("token")

    user = User.query.filter(User.id == id).first()
    user.token = token
    db.session.commit()
    return jsonify(success=1)#1为成功，0为失败

#登录step1
#param：username password
#return BackUser and docterinfo,错误时id=0
@app.route('/login1',methods=['POST'])
def login1():
    username = request.form.get("username")
    password = request.form.get("password")

    user = User.query.filter(User.username == username).first()
    docterinfo = Docterinfo(hospital="0", department="0", position="0", domains="0",name="0", doc_url="0", isavailable=False)
    docterinfo.id = 0
    if user:
        if user.password == password:

            if user.isdocter == True:
                docterinfo = user.docinfo

            user = class_to_dict(User_to_BackUser(user))
            docterinfo = class_to_dict(docterinfo)
            return jsonify(user=user,docterinfo = docterinfo)
    erroruser = BackUser(0,"0","0","0",False)
    docterinfo = class_to_dict(docterinfo)
    return jsonify(user = class_to_dict(erroruser),docterinfo = docterinfo)

#登录step2,获取所有user设置头像
@app.route('/login2')
def login2():
    users = User.query.filter().all()
    users = Users_to_BackUsers(users)
    print(users)
    users = class_to_dict(users)
    return jsonify(users = users)

#修改用户名
@app.route('/changeusername',methods=['POST'])
def changeusername():
    id = request.form.get("id")
    username = request.form.get("username")

    user = User.query.filter(User.id == id).first()
    user1 = User.query.filter(User.username == username).first()
    if user1:
        return jsonify(success=0)
    else:
        user.username = username
        orders = user.mydocterorders
        for i in orders:
            i.patient_name = username
        db.session.commit()
        return jsonify(success=1)



#修改密码
@app.route('/changepassword',methods=['POST'])
def changepassword():
    id = request.form.get("id")
    password = request.form.get("password")

    user = User.query.filter(User.id == id).first()
    user.password = password
    db.session.commit()
    return jsonify(success=1)


#要上传图片时获取临时token
@app.route('/getpicturetoken',methods=['POST'])
def getpicturetoken():
    key = request.form.get("key")
    # 生成上传 Token，可以指定过期时间等

    # 上传策略示例
    # https://developer.qiniu.com/kodo/manual/1206/put-policy
    policy = {
        # 'callbackUrl':'https://requestb.in/1c7q2d31',
        # 'callbackBody':'filename=$(fname)&filesize=$(fsize)'
        # 'persistentOps':'imageView2/1/w/200/h/200'
    }

    # 3600为token过期时间，秒为单位。3600等于一小时
    token = q.upload_token(bucket_name, key, 3600, policy)
    return jsonify(token=token)


#改头像
@app.route('/changehead_url',methods=['POST'])
def changehead_url():
    id = request.form.get("id")
    head_url = request.form.get("head_url")

    user = User.query.filter(User.id == id).first()
    user.head_url = head_url
    db.session.commit()
    return jsonify(success=1)


#内部人员调用的，程序中不会出现
@app.route('/bedocter',methods=['POST'])
def bedocter():
    key = request.form.get("key")#自己设的密码
    id = request.form.get("id")
    hospital = request.form.get("hospital")
    department = request.form.get("department")
    position = request.form.get("position")
    domains = request.form.get("domains")
    name = request.form.get("name")
    doc_url = request.form.get("doc_url")

    user = User.query.filter(User.id == id).first()
    if key != "123" or user == None:
        return jsonify(success=0)

    docterinfo = Docterinfo(hospital=hospital,department=department,position=position,domains=domains,name=name,doc_url=doc_url,isavailable=False)
    user.isdocter = True
    user.docinfo = docterinfo
    db.session.commit()
    return jsonify(success=1)

#医生改变是否在线状态
@app.route('/changeisavailable',methods=['POST'])
def changeisavailable():
    id = request.form.get("id")

    docterinfo = Docterinfo.query.filter(Docterinfo.id == id).first()
    if docterinfo.isavailable == False:
        docterinfo.isavailable = True

    else:
        docterinfo.isavailable = False
    db.session.commit()
    return jsonify(success=1)

#获取在线医生
@app.route('/getdocters')
def getdocters():
    department = request.args.get("department")
    filter = request.args.get("filter")

    if filter == "":
        filter = None
    if department == "":
        department = None


    #无条件
    if filter ==None and department == None:
        docters = Docterinfo.query.filter(Docterinfo.isavailable == True).all()
        docters = class_to_dict(docters)
        return jsonify(docters = docters)
    #科室查询
    elif filter == None and department != None:
        docters = Docterinfo.query.filter(Docterinfo.department == department,Docterinfo.isavailable == True).all()
        docters = class_to_dict(docters)
        return jsonify(docters = docters)
    #按输入查询
    elif filter != None and department == None:
        docters = Docterinfo.query.filter(or_(Docterinfo.name.contains(filter),
                                              Docterinfo.domains.contains(filter),
                                              Docterinfo.hospital.contains(filter),
                                              Docterinfo.department.contains(filter),
                                              Docterinfo.position.contains(filter),),
                                          Docterinfo.isavailable == True).all()
        docters = class_to_dict(docters)
        return jsonify(docters = docters)
    #输入加科室
    else:
        docters = Docterinfo.query.filter(or_(Docterinfo.name.contains(filter),
                                              Docterinfo.domains.contains(filter),
                                              Docterinfo.hospital.contains(filter),
                                              Docterinfo.position.contains(filter), ),
                                          Docterinfo.department == department,
                                          Docterinfo.isavailable == True).all()
        docters = class_to_dict(docters)
        return jsonify(docters = docters)


#添加一个订单
@app.route('/addOne_Order',methods=['POST'])
def addOne_Order():
    patient_id = request.form.get("patient_id")
    docter_id = request.form.get("docter_id")
    print(docter_id)
    patient_user = User.query.filter(User.id == patient_id).first()
    docterinfo = Docterinfo.query.filter(Docterinfo.id == docter_id).first()
    one_order = One_Order(docter_name=docterinfo.name,patient_name=patient_user.username,docter_id=docter_id,patient_id=patient_id)
    db.session.add(one_order)
    db.session.commit()
    return jsonify(success = 1)

#病人打开自己的订单
@app.route('/getmydocterorders')
def getmydocterorders():
    id = request.args.get("id")
    user = User.query.filter(User.id == id).first()
    mydocterorders = user.mydocterorders
    mydocterorders = bytelist(mydocterorders)
    return jsonify(mydocterorders = mydocterorders)

#医生打开自己的订单
@app.route('/mypatientorders')
def getmypatientorders():
    id = request.args.get("id")
    doc = Docterinfo.query.filter(Docterinfo.id == id).first()
    mypatientorders = doc.mypatientorders
    mypatientorders = bytelist(mypatientorders)
    return jsonify(mypatientorders = mypatientorders)


#病人点击订单获得医生的user信息，然后可以对话
@app.route('/getuserfromdoc')
def getuserfromdoc():
    id = request.args.get("id")

    doc = Docterinfo.query.filter(Docterinfo.id == id).first()
    user = doc.userinfo[0]
    user = User_to_BackUser(user)
    user = class_to_dict(user)
    return jsonify(user)



#病人点击完成订单发布评价
@app.route('/finish_order_comment',methods=['POST'])
def finish_order_comment():
    id = request.form.get("id")
    content = request.form.get("content")




    one_order = One_Order.query.filter(One_Order.id==id).first()

    if one_order.iscommented == True:

        order_comment1 = one_order.oneordercomment[0]
        order_comment = Order_Comment.query.filter(Order_Comment.id == order_comment1.id).first()
        order_comment.content = content
    else:
        user = User.query.filter(User.id == one_order.patient_id).first()
        order_comment = Order_Comment(content=content,docter_id=one_order.docter_id,patient_id=one_order.patient_id,
                                      order_id=id,patient_username=user.username,oc_patient_head_url=user.head_url)
        one_order.iscommented = True;
        db.session.add(order_comment)
    db.session.commit()

    return jsonify(success = 1)



#医生打开自己的主页评价
@app.route('/mypatientordercomments')
def getmypatientordercomments():
    id = request.args.get("id")
    doc = Docterinfo.query.filter(Docterinfo.id == id).first()
    mypatientcomments = doc.mypatientcomments
    mypatientcomments = bytelist(mypatientcomments)
    return jsonify(doctercomments = mypatientcomments)




#通过id获得这个人的信息,实现个人主页
@app.route('/getuserpage')
def getuserpage():
    id = request.args.get("id")

    user = User.query.filter(User.id == id).first()
    if user.isdocter == True:
        docterinfo = user.docinfo
        user = class_to_dict(User_to_BackUser(user))
        docterinfo = class_to_dict(docterinfo)
        return jsonify(user=user, docterinfo=docterinfo)
    docterinfo = Docterinfo(hospital="0", department="0", position="0", domains="0", name="0", doc_url="0",
                            isavailable=False)
    docterinfo.id = 0
    docterinfo = class_to_dict(docterinfo)
    user = class_to_dict(User_to_BackUser(user))
    return jsonify(user=user, docterinfo=docterinfo)


#申请添加好友
@app.route('/apply_for_friend',methods=['POST'])
def apply_for_friend():
    passive_id = request.form.get("passive_id")
    positive_id = request.form.get("positive_id")

    passive_user = User.query.filter(User.id == passive_id).first()
    positive_user = User.query.filter(User.id == positive_id).first()
    passive_user.tomakefriends.append(positive_user)
    db.session.commit()
    return jsonify(success=1)

#对好友申请操作，opt为“1”为同意
@app.route('/handle_friendapply',methods=['POST'])
def handle_friendapply():
    passive_id = request.form.get("passive_id")
    positive_id = request.form.get("positive_id")
    opt = request.form.get("opt")

    passive_user = User.query.filter(User.id == passive_id).first()
    positive_user = User.query.filter(User.id == positive_id).first()
    passive_user.tomakefriends.remove(positive_user)
    if opt == '1':
        passive_user.addfriends(positive_user)
    db.session.commit()
    return jsonify(success=1)

#对我的好友申请列表
@app.route('/friend_apply_list')
def friend_apply_list():
    passive_id = request.args.get("passive_id")

    passive_user = User.query.filter(User.id == passive_id).first()
    my_friend_apply = passive_user.tomakefriends
    my_friend_apply = Users_to_BackUsers(my_friend_apply)
    my_friend_apply = class_to_dict(my_friend_apply)
    return jsonify(users = my_friend_apply)


#我的好友列表
@app.route('/friend_list')
def friend_list():
    id = request.args.get("id")

    user = User.query.filter(User.id == id).first()
    my_friends = user.friends
    my_friends = Users_to_BackUsers(my_friends)
    my_friends = class_to_dict(my_friends)
    return jsonify(users = my_friends)

#删除一个好友
@app.route('/delfriend',methods=['POST'])
def delfriend():
    passive_id = request.form.get("passive_id")
    positive_id = request.form.get("positive_id")

    passive_user = User.query.filter(User.id == passive_id).first()
    positive_user = User.query.filter(User.id == positive_id).first()
    passive_user.friends.remove(positive_user)
    positive_user.friends.remove(passive_user)
    db.session.commit()
    return jsonify(success=1)

#搜索对应用户来添加好友
@app.route('/search_friend_list')
def search_friend_list():
    filter = request.args.get("filter")

    users = User.query.filter(User.username.contains(filter)).all()
    search_friends = Users_to_BackUsers(users)
    search_friends = class_to_dict(search_friends)
    return jsonify(users = search_friends)




#发布一个帖子step1
@app.route('/postapost1',methods=['POST'])
def postapost1():
    id = request.form.get('id')
    title = request.form.get('title')
    content = request.form.get('content')

    post = Post(title=title,content=content,author_id=id)
    db.session.add(post)
    db.session.commit()
    post = post.serialized()
    return jsonify(post)

#发布一个帖子step2，添加图片，可能要反复调用
@app.route('/postapost2',methods=['POST'])
def postapost2():
    id = request.form.get('id')
    pic_url = request.form.get('pic_url')

    picture = Picture(pic_url=pic_url,pic_post_id=id)
    db.session.add(picture)
    db.session.commit()
    return jsonify(success=1)

#获得帖子列表，排序在客户端进行
@app.route('/getposts')
def getposts():
    filter = request.args.get("filter")

    if filter:
        posts = Post.query.filter(or_(Post.title.contains(filter),Post.content.contains(filter))).all()
        posts = bytelist(posts)
        return jsonify(posts = posts)
    posts = Post.query.all()
    posts = bytelist(posts)
    return jsonify(posts=posts)

#发布一个回答step1
@app.route('/post_an_answer1',methods=['POST'])
def post_an_answer1():
    author_id = request.form.get('author_id')
    content = request.form.get('content')
    post_id = request.form.get('post_id')

    answer = Answer(content=content,author_id=author_id,post_id=post_id)
    db.session.add(answer)
    db.session.commit()
    answer = answer.serialized()
    return jsonify(answer)

#发布一个回答step2,图片，可能会调用多次
@app.route('/post_an_answer2',methods=['POST'])
def post_an_answer2():
    id = request.form.get('id')
    pic_url = request.form.get('pic_url')

    picture = Picture(pic_url=pic_url, pic_answer_id=id)
    db.session.add(picture)
    db.session.commit()
    return jsonify(success=1)

#发布一个评论
@app.route('/post_a_comment',methods=['POST'])
def post_a_comment():
    author_id = request.form.get('author_id')
    content = request.form.get('content')
    answer_id = request.form.get('answer_id')

    comment = Comment(content=content, author_id=author_id, answer_id=answer_id)
    db.session.add(comment)
    db.session.commit()
    comment = comment.serialized()
    return jsonify(comment)

#我发布的帖子
@app.route('/get_my_post')
def get_my_post():
    id = request.args.get('id')
    user = User.query.filter(User.id == id).first()
    posts = user.myposts
    posts = bytelist(posts)
    return jsonify(posts = posts)

#我发布的回答
@app.route('/get_my_answer')
def get_my_answer():
    id = request.args.get('id')
    user = User.query.filter(User.id == id).first()
    answers = user.myanswers
    answers = bytelist(answers)
    return jsonify(answers = answers)


#收藏一个帖子
@app.route('/like_a_post',methods=['POST'])
def like_a_post():
    id = request.form.get('id')
    post_id = request.form.get('post_id')
    user = User.query.filter(User.id == id).first()
    post = Post.query.filter(Post.id == post_id).first()
    user.likedposts.append(post)
    db.session.commit()
    return jsonify(success=1)

#取消收藏的一个帖子
@app.route('/dellikedpost',methods=['POST'])
def dellikedpost():
    id = request.form.get('id')
    post_id = request.form.get('post_id')
    user = User.query.filter(User.id == id).first()
    post = Post.query.filter(Post.id == post_id).first()
    user.likedposts.remove(post)
    db.session.commit()
    return jsonify(success=1)

#我收藏的帖子
@app.route('/get_my_likedpost')
def get_my_likedpost():
    id = request.args.get('id')
    user = User.query.filter(User.id == id).first()
    posts = user.likedposts
    posts = bytelist(posts)
    return jsonify(posts = posts)

#查看我是否已收藏及帖子收藏数
@app.route('/likedpost_num')
def likedpost_num():
    id = request.args.get('id')
    post_id = request.args.get('post_id')
    user = User.query.filter(User.id == id).first()
    post = Post.query.filter(Post.id == post_id).first()
    users = post.likedusers

    if user in users:
        return jsonify(num = len(users),success=1)
    else:
        return jsonify(num = len(users),success=0)


# 改变我对收藏帖子的状态
@app.route('/dianzan_likepost_change',methods=['POST'])
def dianzan_likepost_change():
    id = request.form.get('id')
    post_id = request.form.get('post_id')
    user = User.query.filter(User.id == id).first()
    post = Post.query.filter(Post.id == post_id).first()
    users = post.likedusers

    if user in users:
        users.remove(user)
        db.session.commit()
        return jsonify(num=len(users), success=0)
    else:
        users.append(user)
        db.session.commit()
        return jsonify(num=len(users), success=1)



#打开一个帖子
@app.route('/open_a_post',methods=['POST'])
def open_a_post():
    id = request.form.get('id')
    post_id = request.form.get('post_id')

    user = User.query.filter(User.id == id).first()
    post = Post.query.filter(Post.id == post_id).first()

    #先加入浏览记录
    new_history = User_History(title=post.title,post_id=post_id,user_id=id)
    db.session.add(new_history)
    db.session.commit()

    #构造帖子所需要素
    author = post.author
    backuser_head = User_to_BackUser(author)
    pics_head = post.pics
    backhead = BackHead(backuser=backuser_head,post=post,picture_urls=pics_head)

    answers = post.myanswers
    print(answers)
    #构造BackAnswers
    backanswers = []
    for i in answers:
        comments = i.mycomments
        print(comments)
        backcomments = []
        for j in comments:
            author_comment = User_to_BackUser(j.author)
            abackcomment = Backcomment(backuser=author_comment,comment=j)
            backcomments.append(abackcomment)

        author_answer = User_to_BackUser(i.author)
        pics_answer = i.pics
        abackanswer = Backanswer(backuser=author_answer,answer=i,picture_urls=pics_answer,comments=backcomments)
        backanswers.append(abackanswer)

    print(backanswers)

    backpost =BackPost(head=backhead,answers=backanswers)
    backpost = backpost.serialized()
    return jsonify( backpost)

#我浏览的历史记录
@app.route('/get_my_sawposts')
def get_my_sawposts():
    id = request.args.get('id')
    user = User.query.filter(User.id == id).first()
    history = user.mysawhistory
    history = bytelist(history)
    return jsonify(history = history)


#清空我浏览的历史记录
@app.route('/del_all_my_sawposts',methods=['POST'])
def del_all_my_sawposts():
    id = request.form.get('id')
    user = User.query.filter(User.id == id).first()
    tmp = user.mysawhistory
    for i in tmp:
        db.session.delete(i)
    db.session.commit()
    return jsonify(success=1)

#通过post_id找到作者user
@app.route('/find_post_user')
def find_post_user():
    post_id = request.args.get('post_id')
    post = Post.query.filter(Post.id == post_id).first()

    user = User.query.filter(User.id == post.author_id).first()
    user = User_to_BackUser(user)
    user = user.serialized()
    return jsonify(user)

#通过post_id找到post主要信息
@app.route('/find_post_info')
def find_post_info():
    post_id = request.args.get('post_id')
    post = Post.query.filter(Post.id == post_id).first()
    post = post.serialized()

    return jsonify(post)



#查看我是否已点赞及医生点赞数
@app.route('/dianzan_doc_num')
def dianzan_doc_num():
    id = request.args.get('id')
    doc_id = request.args.get('doc_id')
    user = User.query.filter(User.id == id).first()
    doc = Docterinfo.query.filter(Docterinfo.id == doc_id).first()
    users = doc.p_dianzan_docs

    if user in users:
        return jsonify(num = len(users),success=1)
    else:
        return jsonify(num = len(users),success=0)

#改变我对医生的点赞状态
@app.route('/dianzan_doc_change',methods=['POST'])
def dianzan_doc_change():
    id = request.form.get('id')
    doc_id = request.form.get('doc_id')
    user = User.query.filter(User.id == id).first()
    doc = Docterinfo.query.filter(Docterinfo.id == doc_id).first()
    users = doc.p_dianzan_docs

    if user in users:
        users.remove(user)
        db.session.commit()
        return jsonify(num = len(users),success=0)
    else:
        users.append(user)
        db.session.commit()
        return jsonify(num = len(users),success=1)





#查看我是否已点赞及帖子点赞数
@app.route('/dianzan_post_num')
def dianzan_post_num():
    id = request.args.get('id')
    post_id = request.args.get('post_id')
    user = User.query.filter(User.id == id).first()
    post = Post.query.filter(Post.id == post_id).first()
    users = post.p_dianzan_posts

    if user in users:
        return jsonify(num = len(users),success=1)
    else:
        return jsonify(num = len(users),success=0)


#改变我对帖子的点赞状态
@app.route('/dianzan_post_change',methods=['POST'])
def dianzan_post_change():
    id = request.form.get('id')
    post_id = request.form.get('post_id')
    user = User.query.filter(User.id == id).first()
    post = Post.query.filter(Post.id == post_id).first()
    users = post.p_dianzan_posts

    if user in users:
        users.remove(user)
        db.session.commit()
        return jsonify(num = len(users),success=0)
    else:
        users.append(user)
        db.session.commit()
        return jsonify(num = len(users),success=1)


# 查看我是否已点赞及回答点赞数
@app.route('/dianzan_answer_num')
def dianzan_answer_num():
    id = request.args.get('id')
    answer_id = request.args.get('answer_id')
    user = User.query.filter(User.id == id).first()
    answer = Answer.query.filter(Answer.id == answer_id).first()
    users = answer.p_dianzan_answers

    if user in users:
        return jsonify(num=len(users), success=1)
    else:
        return jsonify(num=len(users), success=0)


# 改变我对回答的点赞状态
@app.route('/dianzan_answer_change',methods=['POST'])
def dianzan_answer_change():
    id = request.form.get('id')
    answer_id = request.form.get('answer_id')
    user = User.query.filter(User.id == id).first()
    answer = Answer.query.filter(Answer.id == answer_id).first()
    users = answer.p_dianzan_answers

    if user in users:
        users.remove(user)
        db.session.commit()
        return jsonify(num=len(users), success=0)
    else:
        users.append(user)
        db.session.commit()
        return jsonify(num=len(users), success=1)


# 查看我是否已点赞及评论点赞数
@app.route('/dianzan_comment_num')
def dianzan_comment_num():
    id = request.args.get('id')
    comment_id = request.args.get('comment_id')
    user = User.query.filter(User.id == id).first()
    comment = Comment.query.filter(Comment.id == comment_id).first()
    users = comment.p_dianzan_comments

    if user in users:
        return jsonify(num=len(users), success=1)
    else:
        return jsonify(num=len(users), success=0)


# 改变我对回答的评论状态
@app.route('/dianzan_comment_change',methods=['POST'])
def dianzan_comment_change():
    id = request.form.get('id')
    comment_id = request.form.get('comment_id')
    user = User.query.filter(User.id == id).first()
    comment = Comment.query.filter(Comment.id == comment_id).first()
    users = comment.p_dianzan_comments

    if user in users:
        users.remove(user)
        db.session.commit()
        return jsonify(num=len(users), success=0)
    else:
        users.append(user)
        db.session.commit()
        return jsonify(num=len(users), success=1)

if __name__ == '__main__':
    app.run(host = "0.0.0.0",debug= True)
