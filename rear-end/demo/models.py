from datetime import datetime

from exts import db


SQLALCHEMY_TRACK_MODIFICATIONS = False


user_user = db.Table("user_user",
                        db.Column("first_id",db.Integer,db.ForeignKey("user.id"),primary_key=True),
                        db.Column("second_id",db.Integer,db.ForeignKey("user.id"),primary_key=True),
                       )

friend_apply = db.Table("friend_apply",
                        db.Column("passive_id",db.Integer,db.ForeignKey("user.id"),primary_key=True),
                        db.Column("positive_id",db.Integer,db.ForeignKey("user.id"),primary_key=True),
                       )


user_post = db.Table("user_post",
                        db.Column("user_id",db.Integer,db.ForeignKey("user.id"),primary_key=True),
                        db.Column("post_id",db.Integer,db.ForeignKey("post.id"),primary_key=True),
                       )

dianzan_doc = db.Table("dianzan_doc",
                        db.Column("user_id",db.Integer,db.ForeignKey("user.id"),primary_key=True),
                        db.Column("docterinfo_id",db.Integer,db.ForeignKey("docterinfo.id"),primary_key=True),
                       )

dianzan_post = db.Table("dianzan_post",
                        db.Column("user_id",db.Integer,db.ForeignKey("user.id"),primary_key=True),
                        db.Column("post_id",db.Integer,db.ForeignKey("post.id"),primary_key=True),
                       )

dianzan_answer = db.Table("dianzan_answer",
                        db.Column("user_id",db.Integer,db.ForeignKey("user.id"),primary_key=True),
                        db.Column("answer_id",db.Integer,db.ForeignKey("answer.id"),primary_key=True),
                       )

dianzan_comment = db.Table("dianzan_comment",
                        db.Column("user_id",db.Integer,db.ForeignKey("user.id"),primary_key=True),
                        db.Column("comment_id",db.Integer,db.ForeignKey("comment.id"),primary_key=True),
                       )


class User_History(db.Model):
    __tablename__ = "user_history"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    create_time = db.Column(db.DateTime, default=datetime.now, nullable=False)

    title = db.Column(db.String(100), nullable=False)
    post_id = db.Column(db.Integer, db.ForeignKey("post.id"), nullable=False)
    post = db.relationship('Post', backref=db.backref("mysawhistory"))

    user_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    user = db.relationship('User', backref=db.backref("mysawhistory"))

    def serialized(self):
        return {
            'id': self.id,
            'title':self.title,
            'post_id':self.post_id,
            'create_time':str(self.create_time),
            'user_id':self.user_id
        }



class Order_Comment(db.Model):
    __tablename__ = "order_comment"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    content = db.Column(db.Text, nullable=False)
    create_time = db.Column(db.DateTime, default=datetime.now, nullable=False)
    patient_username = db.Column(db.String(100), nullable=False)
    oc_patient_head_url = db.Column(db.String(100), nullable=False)



    # 医生存的订单评价
    docter_id = db.Column(db.Integer, db.ForeignKey("docterinfo.id"))
    docter = db.relationship('Docterinfo', backref=db.backref("mypatientcomments"))
    #病人存的订单评价
    patient_id = db.Column(db.Integer, db.ForeignKey("user.id"))
    patient = db.relationship('User', backref=db.backref("mydoctercomments"))
    #是哪个病单的评价
    order_id = db.Column(db.Integer, db.ForeignKey("one_order.id"))
    myorder = db.relationship('One_Order', backref=db.backref("oneordercomment"))


    def serialized(self):
        return {
            'id': self.id,
            'content':self.content,
            'create_time':str(self.create_time),
            'docter_id':self.docter_id,
            'patient_id':self.patient_id,
            'order_id':self.order_id,
            'patient_username':self.patient_username,
            'patient_head_url':self.oc_patient_head_url
        }


class One_Order(db.Model):
    __tablename__ = "one_order"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    docter_name = db.Column(db.String(100), nullable=False)
    patient_name = db.Column(db.String(100), nullable=False)
    create_time = db.Column(db.DateTime, default=datetime.now, nullable=False)
    iscommented = db.Column(db.Boolean,default=False,nullable=False)


    #医生存的订单
    docter_id = db.Column(db.Integer, db.ForeignKey("docterinfo.id"))
    docter = db.relationship('Docterinfo', backref=db.backref("mypatientorders"))
    #病人存的订单
    patient_id = db.Column(db.Integer, db.ForeignKey("user.id"))
    patient = db.relationship('User', backref=db.backref("mydocterorders"))





    def serialized(self):
        return {
            'id': self.id,
            'docter_name':self.docter_name,
            'patient_name':self.patient_name,
            'create_time':str(self.create_time),
            'iscommented':self.iscommented,
            'docter_id':self.docter_id,
            'patient_id':self.patient_id
        }

class User(db.Model):
    __tablename__ = "user"
    id = db.Column(db.Integer,primary_key=True,autoincrement=True)
    username = db.Column(db.String(100), nullable=False)
    password = db.Column(db.String(100), nullable=False)
    head_url = db.Column(db.String(100), nullable=False)
    token = db.Column(db.String(150))
    isdocter = db.Column(db.Boolean,nullable=False)

    #好友
    friends = db.relationship("User",secondary=user_user,primaryjoin=(id==user_user.c.first_id),
                              secondaryjoin=(id==user_user.c.second_id))
    #向我申请的好友
    tomakefriends = db.relationship("User", secondary=friend_apply, primaryjoin=(id == friend_apply.c.passive_id),
                              secondaryjoin=(id == friend_apply.c.positive_id))




    #医生认证
    docinfo_id = db.Column(db.Integer, db.ForeignKey("docterinfo.id"))
    docinfo = db.relationship('Docterinfo', backref=db.backref("userinfo"))

    #收藏帖子
    likedposts = db.relationship("Post",secondary = user_post,backref = "likedusers")

    #点赞

    dianzan_docs = db.relationship("Docterinfo", secondary=dianzan_doc, backref="p_dianzan_docs")
    dianzan_posts = db.relationship("Post", secondary=dianzan_post, backref="p_dianzan_posts")
    dianzan_answers = db.relationship("Answer", secondary=dianzan_answer, backref="p_dianzan_answers")
    dianzan_comments = db.relationship("Comment", secondary=dianzan_comment, backref="p_dianzan_comments")


    def addfriends(self,user):
        self.friends.append(user)
        user.friends.append(self)




class Docterinfo(db.Model):
    __tablename__ = "docterinfo"
    id = db.Column(db.Integer,primary_key=True,autoincrement=True)
    hospital = db.Column(db.String(100), nullable=False)
    department = db.Column(db.String(100), nullable=False)
    position = db.Column(db.String(100), nullable=False)
    domains = db.Column(db.String(250), nullable=False)
    name = db.Column(db.String(100), nullable=False)
    doc_url = db.Column(db.String(100), nullable=False)

    isavailable = db.Column(db.Boolean,nullable=False)




class Post(db.Model):
    __tablename__ = "post"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    title = db.Column(db.String(100), nullable=False)
    content = db.Column(db.Text, nullable=False)
    create_time = db.Column(db.DateTime,default=datetime.now,nullable=False)

    #自己写的帖子
    author_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    author = db.relationship('User', backref=db.backref("myposts"))

    def serialized(self):
        return {
            'id': self.id,
            'title': self.title,
            'content': self.content,
            'create_time': str(self.create_time),

            'author_id': self.author_id,
        }



class Answer(db.Model):
    __tablename__ = "answer"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    content = db.Column(db.Text, nullable=False)
    create_time = db.Column(db.DateTime, default=datetime.now, nullable=False)

    # 自己写的回答
    author_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    author = db.relationship('User', backref=db.backref("myanswers"))

    # 是属于哪个帖子
    post_id = db.Column(db.Integer, db.ForeignKey("post.id"), nullable=False)
    post = db.relationship('Post', backref=db.backref("myanswers"))

    def serialized(self):
        return {
            'id': self.id,
            'content': self.content,
            'create_time': str(self.create_time),
            'author_id': self.author_id,

            'post_id': self.post_id
        }

class Comment(db.Model):
    __tablename__ = "comment"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    content = db.Column(db.String(100), nullable=False)
    create_time = db.Column(db.DateTime, default=datetime.now, nullable=False)

    # 自己写的评论
    author_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    author = db.relationship('User', backref=db.backref("mycomments"))

    # 回答的图片
    answer_id = db.Column(db.Integer, db.ForeignKey("answer.id"))
    answer = db.relationship('Answer', backref=db.backref("mycomments"))

    def serialized(self):
        return {
            'id': self.id,
            'content': self.content,
            'create_time': str(self.create_time),

            'author_id': self.author_id,
            'answer_id': self.answer_id
        }

class Picture(db.Model):
    __tablename__ = "picture"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    pic_url = db.Column(db.String(100), nullable=False)

    # 帖子的图片
    pic_post_id = db.Column(db.Integer, db.ForeignKey("post.id"))
    pic_post = db.relationship('Post', backref=db.backref("pics"))

    # 回答的图片
    pic_answer_id = db.Column(db.Integer, db.ForeignKey("answer.id"))
    pic_answer = db.relationship('Answer', backref=db.backref("pics"))

    def serialized(self):
        return {
            'id': self.id,
            'pic_url': self.pic_url,
        }