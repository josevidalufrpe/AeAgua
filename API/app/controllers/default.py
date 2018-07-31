# coding=utf-8
from flask import request
from app import carropipa
from flask import render_template
from flask import flash
from flask_login import login_user
from flask_login import logout_user
from app import db
from app import lm

#import das tabelas
from app.models.forms import LoginForm
from app.models.forms import CadastrarForm
from app.models.tables import User
from app.models.tables import Pessoa
from app.models.tables import PessoaFisica
from app.models.tables import PessoaJuridica
from app.models.tables import Cliente
from app.models.tables import Endereco
from app.models.tables import Motorista


@carropipa.route("/")
def index():
    return render_template('index.html')


@carropipa.route("/cadastro/cadastrar", methods=["GET","POST"])
def registrar():
    if request.method=="POST":
        login = request.form.get("usermail")
        senha = request.form.get('senha')
        #informação de pessoa
        nome = request.form.get('nome')
        sobrenome = request.form.get('sobrenome')
        telefone = request.form.get('telefone')
        cpf = request.form.get('cpf')
        #informação de perfil
        logradouro = request.form.get('logradouro')
        complemento = request.form.get('complemento')
        bairro = request.form.get('bairro')
        cep = request.form.get('cep')
        cidade = request.form.get('cidade')
        uf = request.form.get('uf')
        tipo = request.form.get('tipo')

        i = User(login, senha)
        db.session.add(i)
        db.session.commit()
    
        #recuperar o id que foi inserido no banco
        u = User.query.filter_by(usermail=login).first()
        if u and tipo=="Pessoa Física":
            user_id = u.id
            i = Pessoa(tipo, user_id)
            db.session.add(i)
            db.session.commit()
            u = Pessoa.query.filter_by(user_id=u.id).first()
            pessoa_id = u.id
            i = PessoaFisica("0", nome,sobrenome,pessoa_id,telefone)
            db.session.add(i)
            db.session.commit()
            i = Cliente(None, 0, user_id, pessoa_id )
            db.session.add(i)
            db.session.commit()
            #c = Cliente.query.filter_by(pessoa_id=u.id).first()
            return "cadastration_ok,{},{}".format(nome, sobrenome)  #um dia retornar .Json
        
        elif u and tipo=="Pessoa Juridica":
            print("PESSOA FISICA")
            user_id = u.id
            i = Pessoa(tipo, user_id)
            db.session.add(i)
            db.session.commit()
            u = Pessoa.query.filter_by(user_id=u.id).first()
            pessoa_id = u.id
            i = PessoaJuridica(None,nome,pessoa_id)
            db.session.add(i)
            db.session.commit()
            i = Cliente(None, 0, user_id, pessoa_id )
            db.session.add(i)
            db.session.commit()
            #c = Cliente.query.filter_by(pessoa_id=u.id).first()
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
            i = PessoaFisica(None, nome,sobrenome,pessoa_id,celular)
            db.session.add(i)
            db.session.commit()
            i = Motorista(cnh, '0',0, user_id, pessoa_id )
            db.session.add(i)
            db.session.commit()
            return "cadastration_ok,{},{}".format(nome, sobrenome)  #um dia retornar .Json
        else:
            return "cadastration_fail"


@carropipa.route("/cadastro/criar_perfil", methods=["GET","POST"])
def criar_perfil():
    
    if request.method=="POST":
        login = request.form.get("usermail")
        senha = request.form.get('senha')
        print("login:",login)
        #informação de pessoa
        nome = request.form.get('nome')
        sobrenome = request.form.get('sobrenome')
        telefone = request.form.get('telefone')
        cpf = request.form.get('cpf')
        #informação de perfil
        logradouro = request.form.get('logradouro')
        complemento = request.form.get('complemento')
        bairro = request.form.get('bairro')
        cep = request.form.get('cep')
        cidade = request.form.get('cidade')
        uf = request.form.get('uf')

        #printando
        u = User.query.filter_by(usermail = login,password = senha).first()
        print("usuário",u)
        if u:
            p = Pessoa.query.filter_by(user_id=u.id).first()
            print(p)
            if p.tipo=='Pessoa Física':
                pf = PessoaFisica.query.filter_by(pessoa_id = p.id).update(dict(cpf=cpf))
                db.session.commit()
                e = Endereco.query.filter_by(pessoa_id = p.id).first()
                if e:
                    e = Endereco.query.filter_by(pessoa_id = p.id).update(dict(logradouro = logradouro, complemento = complemento, bairro = bairro, cep = cep, cidade = cidade, uf = uf))
                else:    
                    e = Endereco(logradouro, complemento, bairro, cep, cidade, uf, p.id)
                    db.session.add(e)
                db.session.commit()
                return "cadastration_ok,"
            else:
                return "cadastration_fail, pessoa não encontrado"
        else:
             return "cadastration_fail, usuario não encontrado"
    else:
        return "cadastration_fail, metodo não permitido"

'''
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
'''


@carropipa.route("/login/logar", methods=['GET', 'POST'])
def logar():
    if request.method=='POST':
        p_tipo = True
        login,senha = request.form.get('email'), request.form.get('senha')
        u = User.query.filter_by(usermail=login).first()
        if u and u.password == senha:
            p = Pessoa.query.filter_by(user_id=u.id).first()
            c =  Cliente.query.filter_by(user_id=u.id).first()
            if p.tipo == "Pessoa Física":
                p_tipo = PessoaFisica.query.filter_by(pessoa_id=p.id).first()
                if p_tipo.cpf == '0' or p_tipo.telefone == 'x xxxx xxxx':
                    #       0                                           1       2           3      4            5               6      7      8       
                    return "login_ok,{},{},{},{},{},{},{},{}".format(u.id,u.usermail,u.password,p.id, p_tipo.nome, p_tipo.sobrenome,c.id,c.rank )
                else:
                    return "login_ok,{},{},{},{},{},{},{},{},{},{}".format(u.id,u.usermail,u.password,p.id, p_tipo.nome, p_tipo.sobrenome,p_tipo.cpf,p_tipo.telefone,c.id,c.rank )
            else:
                #deveria ser juridica
                p_tipo = PessoaJuridica.query.filter_by(pessoa_id=p.id).first()
                if p_tipo.cnpj == '0':
                    return "login_ok,{},{},{},{},{},{},{}".format(u.id,u.usermail,u.password,p.id, p_tipo.nome,c.id,c.rank )
                else:
                    return "login_ok,{},{},{},{},{},{},{},{}".format(u.id,u.usermail,u.password,p.id, p_tipo.nome,p_tipo.cnpj,c.id,c.rank )
        else:
            return "login_denied"


@carropipa.route("/login/logar_motorista", methods=['GET', 'POST'])
def logar_motorista():
    if request.method=='POST':
            login,senha = request.form.get('email'), request.form.get('senha')
            u = User.query.filter_by(usermail=login).first()
            if u and u.password == senha:
                return "login_ok,{},{}".format(u.usermail, u.password)
            else:
                return "login_denied"


"""
@carropipa.route("/perfil", methods=["post","get"])
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

#GET PERFIL N PRECISA SER ROTA
@carropipa.route("/login/getperfil", methods=["post","get"])
def getPerfil():
    if request.method=='POST':
        p_tipo = True
        login,senha = request.form.get('email'), request.form.get('senha')
        u = User.query.filter_by(usermail=login).first()
        if u and u.password == senha:
            p = Pessoa.query.filter_by(user_id=u.id).first()
            c =  Cliente.query.filter_by(user_id=u.id).first()
            if p.tipo == "Pessoa Física":
                p_tipo = PessoaFisica.query.filter_by(pessoa_id=p.id).first()
                if p_tipo.cpf == '0' or p_tipo.telefone == 'x xxxx xxxx':
                    print("login_ok,{},{},{},{},{},{},{},{},{}".format(u.id,u.usermail,u.password,p.id, p_tipo.nome, p_tipo.sobrenome,c.id,c.rank, p_tipo.cpf ))
                    return "login_ok,{},{},{},{},{},{},{},{},{}".format(u.id,u.usermail,u.password,p.id, p_tipo.nome, p_tipo.sobrenome,c.id,c.rank, p_tipo.cpf )
                else:
                    return "login_ok,{},{},{},{},{},{},{},{},{},{}".format(u.id,u.usermail,u.password,p.id, p_tipo.nome, p_tipo.sobrenome,p_tipo.cpf,p_tipo.telefone,c.id,c.rank )        
            else:
                #deveria ser juridica
                p_tipo = PessoaJuridica.query.filter_by(pessoa_id=p.id).first()
                if p_tipo.cnpj == '0':
                    return "login_ok,{},{},{},{},{},{},{}".format(u.id,u.usermail,u.password,p.id, p_tipo.nome,c.id,c.rank )
                else:
                    return "login_ok,{},{},{},{},{},{},{},{}".format(u.id,u.usermail,u.password,p.id, p_tipo.nome,p_tipo.cnpj,c.id,c.rank )


#SE o cliente vai ser atualizado:
@carropipa.route("/atualizar/atualizarperfil", methods=["post","get"])
def atualizar():
    if request.method=='POST':
        client_id = request.form.get("id")
        cliente = Cliente.query.filter_by(id=client_id).first()
        if cliente:
            #temos o usuário no banco > pegar os dados do form
            # TODO: JSON
            client_nome = request.form.get("nome")
            client_email = request.form.get("usermail")
            client_sobreNome = request.form.get("sobreNome")
            client_senha = request.form.get("senha")
            client_rank = request.form.get("rank")
            client_telefone = request.form.get("telefone")
            client_cpf = request.form.get("cpf")
            client_logradouro = request.form.get("logradouro")
            client_complemento = request.form.get("complemento")
            client_cidade = request.form.get("cidade")
            client_cep = request.form.get("cep")
            client_uf = request.form.get("uf")
            client_tipo = request.form.get("tipo")
            #descobrindo  o usuário
            usuario = User.query.filter_by(id=cliente.user_id).first()
            #atualiza os dados nos objetos especificos:
            if usuario and client_tipo == "Pessoa Física":
                pf = Pessoa.query.filter_by(id=cliente.pessoa_id).first().id
                pessoa = PessoaFisica.query.filter_by(pessoa_id=pf).first()
                #alimento os dados de pessoa:
                if pessoa:
                    pessoa.cpf = client_cpf
                    pessoa.nome = client_nome
                    pessoa.sobrenome = cliente_sobreNome
                    pessoa.telefone = cliente_telefone
                    return "cadastration_ok,"
                else:
                    return "cadastration_fail, pessoa não encontrado"
            else:
                #tabela juridicapf = Pessoa.query.filter_by(id=cliente.pessoa_id).first().id
                pessoa = PessoaJuridica.query.filter_by(pessoa_id=pf).first()
                #aliemnto os dados de pessoa:
                if pessoa:
                    pessoa.cnpj = client_cpf
                    pessoa.nome = client_nome
                    return "cadastration_ok,"
                else:
                    return "cadastration_fail, pessoa não encontrado"
        else:
            return "cadastration_fail, pessoa não encontrado"

@carropipa.route("/pedido/fazer", methods=["GET", "POST"])
def fazer_pedido():
    if request.method=="POST":
        
        valor = request.form.get["valor"]
        
        p = Pedido( '00:00', '00:00', valor, 1, 1)
        db.session.add(p)
        db.commit()
        return "Cadastration_ok,"


@carropipa.route("/pedido/ler", methods=["GET", "POST"])
def ler_pedido():
    if request.method=="POST":
        cliente_id = request.form.get["userid"]
        p = Pedido.query.filter_by(client_id = client_id)
        print(type(p), p)
        


                