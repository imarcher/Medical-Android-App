def bytelist(obj):
    res = []
    for o in obj:
        oo = o.serialized()
        res.append(oo)
    return res