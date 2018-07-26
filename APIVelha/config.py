"""
Arquivo de configuração
estão inseridas aqui configurações relativas ao bando de dados, ao # DEBUG:
e a chave de segurança do formulário
"""

import os.path

basedir = os.path.abspath(os.path.dirname(__file__))
SQLALCHEMY_DATABASE_URI = 'sqlite:///'+ os.path.join(basedir,'storage2.db')
SQLALCHEMY_TRACK_MODIFICATIONS = True
DEBUG = True
SECRET_KEY = 'chave-de-seguranca'
