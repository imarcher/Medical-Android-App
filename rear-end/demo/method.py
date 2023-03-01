from  models import User,Order_Comment
from backmodels import BackUser


def class_to_dict(obj):
    is_list = obj.__class__ == [].__class__

    is_set = obj.__class__ == set().__class__

    if is_list or is_set:

        obj_arr = []

        for o in obj:
            print(11111)
            dict = {}

            a = o.__dict__

            if "_sa_instance_state" in a:
                del a['_sa_instance_state']


            dict.update(a)

            obj_arr.append(dict)

        return obj_arr

    else:

        dict = {}

        a = obj.__dict__

        if "_sa_instance_state" in a:
            del a['_sa_instance_state']

        dict.update(a)

        return dict



def User_to_BackUser(user):
    return BackUser(user.id,user.username,user.head_url,user.token,user.isdocter)


def Users_to_BackUsers(users):
    list = []
    for i in users:
        list.append(User_to_BackUser(i))
    return list







