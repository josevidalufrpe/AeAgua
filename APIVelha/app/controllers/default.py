from flask import request
from app import carropipa
from flask import render_template
from flask import flash
from flask_login import login_user
from flask_login import logout_user
from app import db
from app import lm
from app.models.forms import LoginForm
from app.models.forms import CadastrarForm
from app.models.tables import User
from app.models.tables import Pessoa
from app.models.tables import PessoaFisica
from app.models.tables import Cliente



@carropipa.route("/")
def index():
    return render_template('index.html')

@carropipa.route("/cadastro/cadastrar", methods=["GET","POST"])
def registrar():
    if request.method=="POST":
        email = request.form.get('email')
        senha = request.form.get('senha')

        celular = request.form.get('celular')
        sobrenome = request.form.get('sobrenome')
        nome = request.form.get('nome')
        tipo = request.form.get("tipo")

        i = User(email, senha)
        db.session.add(i)
        db.session.commit()
        #recuperar o id que foi inserido no banco
        u = User.query.filter_by(usermail=email).first()
        if u and tipo=="Pessoa Física":
            user_id = u.id
            i = Pessoa(tipo, user_id)
            db.session.add(i)
            db.session.commit()
            u = Pessoa.query.filter_by(user_id=u.id).first()
            pessoa_id = u.id
            i = PessoaFisica(None, nome,sobrenome,pessoa_id)
            db.session.add(i)
            db.session.commit()
            i = Cliente(None, 0, user_id, pessoa_id )
            db.session.add(i)
            db.session.commit()
            return "cadastration_ok,{},{}".format(nome, sobrenome)  #um dia retornar .Json
        else:
            return "cadastration_fail"

@carropipa.route("/cadastro/cadastrar_motorista", methods=["GET","POST"])
def registrar_motorista():
    if request.method=="POST":
        email = request.form.get('email')
        senha = request.form.get('senha')
        cnh = request.form.get('cnh')
        celular = request.form.get('celular')
        sobrenome = request.form.get('sobrenome')
        nome = request.form.get('nome')
        tipo = request.form.get("tipo")

        i = User(email, senha)
        db.session.add(i)
        db.session.commit()
        #recuperar o id que foi inserido no banco
        u = User.query.filter_by(usermail=email).first()
        if u and tipo=="Pessoa Física":
            user_id = u.id
            i = Pessoa(tipo, user_id)
            db.session.add(i)
            db.session.commit()
            u = Pessoa.query.filter_by(user_id=u.id).first()
            pessoa_id = u.id
            i = PessoaFisica(None, nome,sobrenome,pessoa_id)
            db.session.add(i)
            db.session.commit()
            i = Motorista(cnh, '0',0, user_id, pessoa_id )
            db.session.add(i)
            db.session.commit()
            return "cadastration_ok,{},{}".format(nome, sobrenome)  #um dia retornar .Json
        else:
            return "cadastration_fail"
"""
@carropipa.route("/cadastro/criar_perfil", methods=["GET","POST"])
def criar_perfil():
    if request.method=="POST":
        login = request.form.get("usermail")
        print("login:",login)
        nome = request.form.get('nome')
        print("Nome:", nome)
        sobrenome = request.form.get('sobrenome')
        telefone = request.form.get('telefone')
        senha = request.form.get('senha')

        u = User(login, senha)
        db.session.add(u)
        db.session.commit(u)
        print("usuário",u)
        if u:
            p = Pessoa.query.filter_by(user_id=u.id).first()
            print(p)
            if p:
                #i = Pessoa(p.name, p.lastname, cpf, p.telefone, 0, p.user_id)
                db.session.update(dict(cpf=cpf))
                #db.session.commit()
                e = Endereco(logradouro, complemento, bairro, cep, cidade, uf, p.id)
                db.session.add(e)
                db.session.commit()
                return "cadastration_ok,"
            else:
                return "cadastration_fail,"
        else:
             return "cadastration_fail, usuario não encontrado"
    else:
        return "cadastration_fail, metodo não permitido"

"""

@carropipa.route("/cadastrar",  methods=["GET","POST"])
def cadastrar():
    formulario = CadastrarForm()
    if formulario.validate_on_submit():
        usermail = formulario.username.data
        password = formulario.password.data
        i = User(usermail, password)
        db.session.add(i)
        db.session.commit()
        flash('cadastrado com sucesso')

    return render_template('cadastro.html', form=formulario)


@carropipa.route("/login/logar", methods=['GET', 'POST'])
def logar():
    if request.method=='POST':
        p_tipo = True
        login,senha = request.form.get('email'), request.form.get('senha')
        u = User.query.filter_by(usermail=login).first()
        if u and u.password == senha:
            p = Pessoa.query.filter_by(user_id=u.id).first()
            cliente_rank =  Cliente.query.filter_by(user_id=u.id).first().rank
            if p.tipo == "Pessoa Física":
                p_tipo = PessoaFisica.query.filter_by(pessoa_id=p.id).first()
            else:
                p_tipo = PessoaFisica.query.filter_by(pessoa_id=p.id).first()
            return "login_ok,{},{},{},{}".format(u.usermail, p_tipo.nome, p_tipo.sobrenome, cliente_rank )
        else:
            return "login_denied"

"""
@carropipa.route("/login/logar_motorista", methods=['GET', 'POST'])
def logar_motorista():
    if request.method=='POST':
            login,senha = request.form.get('email'), request.form.get('senha')
            u = User.query.filter_by(usermail=login).first()
            if u and u.password == senha:
                return "login_ok,{},{}".format(u.usermail, u.password)
            else:
                return "login_denied"



@carropipa.route("/login", methods=["post","get"])
def login():
    form = LoginForm()
    if form.validate_on_submit():
        u = User.query.filter_by(username=form.username.data).first()
        if u and u.password == form.password.data:
            login_user(u)
        else:
            flash("Invalid Login")
    else:
        print(form.errors)
    return render_template('login.html', form=form)
"""
