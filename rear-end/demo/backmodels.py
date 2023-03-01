from method2 import bytelist

class BackUser:
    def __init__(self,id,username,head_url,token,isdocter):
        self.id = id
        self.username = username
        self.head_url = head_url
        self.token = token
        self.isdocter = isdocter

    def serialized(self):
        return {
            'id': self.id,
            'username': self.username,
            'head_url': self.head_url,
            'token': self.token,
            'isdocter': self.isdocter
        }

class BackPost:
    def __init__(self,head,answers):
        self.head = head
        self.answers = answers

    def serialized(self):
        return {
            'head': self.head.serialized(),
            'answers': bytelist(self.answers)
        }


class BackHead:
    def __init__(self,backuser,post,picture_urls):
        self.backuser = backuser
        self.post = post
        self.picture_urls = picture_urls

    def serialized(self):
        return {
            'user': self.backuser.serialized(),
            'post': self.post.serialized(),
            'picture_urls':bytelist(self.picture_urls)
        }

class Backanswer:
    def __init__(self,backuser,answer,picture_urls,comments):
        self.backuser = backuser
        self.answer = answer
        self.picture_urls = picture_urls
        self.comments = comments

    def serialized(self):
        return {
            'user': self.backuser.serialized(),
            'answer': self.answer.serialized(),
            'picture_urls':bytelist(self.picture_urls),
            'comments':bytelist(self.comments)
        }

class Backcomment:
    def __init__(self, backuser, comment):
        self.backuser = backuser
        self.comment = comment
    def serialized(self):
        return {
            'user': self.backuser.serialized(),
            'comment':self.comment.serialized()
        }

