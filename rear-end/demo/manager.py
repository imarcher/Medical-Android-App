from flask_script import Manager
from flask_migrate import Migrate,MigrateCommand
from start import app
from exts import db
from models import User,Docterinfo,Post,Answer,Comment,Picture,One_Order,User_History,Order_Comment


manager = Manager(app)

#绑定app db
migrate = Migrate(app,db)

#添加迁移脚本

manager.add_command("db",MigrateCommand)

if __name__ == "__main__":
    manager.run()

# python manager.py db init
# python manager.py db migrate//更改
# python manager.py db upgrade//更改
