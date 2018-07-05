from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_script import Manager
from flask_script import Server
from flask_migrate import Migrate
from flask_migrate import MigrateCommand
from flask_login import LoginManager

carropipa = Flask(__name__)

carropipa.config.from_object('config')

db = SQLAlchemy(carropipa)
migrate = Migrate(carropipa, db)

manager = Manager(carropipa)
manager.add_command('db', MigrateCommand)


lm = LoginManager()
lm.init_app(carropipa)

from app.models import tables
from app.models import forms
from app.controllers import default
