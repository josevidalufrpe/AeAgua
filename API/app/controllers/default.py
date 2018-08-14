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
from app.models.tables import Pedido
from app.models.tables import Veiculo

@carropipa.route("/")
def index():
    return render_template('index.html')

@carropipa.route("/cadastro/atualizar", methods=["GET","POST"])
def atualizar():
    if request.method=="POST":
        id = request.form.get('id')
        tipo = request.form.get('tipo')
        cpf = request.form.get('cpf')

        #recuperar o id que foi inserido no banco
        u = User.query.filter_by(id=id).first()
        if u and tipo=="Pessoa Física":
            user_id = u.id
            u = Pessoa.query.filter_by(user_id=u.id).first()
            pessoa_id = u.id
            u = PessoaFisica.query.filter_by(pessoa_id=pessoa_id).first().update({"cpf":cpf}, synchronize_session=False)
            db.session.add(u)
            db.session.commit()
            #c = Cliente.query.filter_by(pessoa_id=u.id).first()
            return "cadastration_ok,"  #um dia retornar .Json

        elif u and tipo=="Pessoa Juridica":

            pass



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
            i = PessoaFisica(None, nome,sobrenome,pessoa_id,celular)
            db.session.add(i)
            db.session.commit()
            i = Cliente(None, 0, user_id, pessoa_id )
            db.session.add(i)
            db.session.commit()
            #c = Cliente.query.filter_by(pessoa_id=u.id).first()
            return "cadastration_ok,{},{}".format(nome, sobrenome)  #um dia retornar .Json
        
        elif u and tipo=="Pessoa Juridica":
            
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

        capacidade = request.form.get("capacidade")
        placa = request.form.get("placa")
        cor = request.form.get("cor")


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
            p = Pessoa.query.filter_by(user_id=u.id).first()
            pf = PessoaFisica(cnh, nome,sobrenome,p.id,celular)
            db.session.add(pf)
            db.session.commit()
            m = Motorista(cnh, '0',0, user_id, p.id )
            db.session.add(m)
            db.session.commit()
            m = Motorista.query.filter_by(user_id=user_id).first()
            print(m.id)
            v = Veiculo(placa,capacidade,cor,m.id)
            db.session.add(v)
            db.session.commit()
            return "cadastration_ok,{},{}".format(nome, sobrenome)  #um dia retornar .Json

        elif u and tipo=="Pessoa Juridica":

            user_id = u.id
            i = Pessoa(tipo, user_id)
            db.session.add(i)
            db.session.commit()
            p = Pessoa.query.filter_by(user_id=u.id).first()
            pj = PessoaJuridica(cnh, nome,p.id)
            db.session.add(pj)
            db.session.commit()
            m = Motorista(cnh, '0',0, user_id, p.id )
            db.session.add(m)
            db.session.commit()
            m = Motorista.query.filter_by(user_id=user_id).first()
            v = Veiculo(placa,capacidade,cor,m.id)
            db.session.add(v)
            db.session.commit()

            return "cadastration_ok,{},{}".format(nome,"null")  #um dia retornar .Json
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
                p = Pessoa.query.filter_by(user_id=u.id).first()
                m =  Motorista.query.filter_by(user_id=u.id).first()
                v = Veiculo.query.filter_by(motorista_id=m.id).first()
                if p.tipo == "Pessoa Física":
                    p_tipo = PessoaFisica.query.filter_by(pessoa_id=p.id).first()
                    return "login_ok,{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}".format(u.id,u.usermail,u.password,p.id, p_tipo.nome, p_tipo.sobrenome,p_tipo.cpf,p_tipo.telefone,m.id,m.rank,m.cnh,v.id,v.placa,v.cor,v.capacidade)
                    #retorna [16]
                else:
                    p_tipo = PessoaJuridica.query.filter_by(pessoa_id=p.id).first()
                    return "login_ok,{},{},{},{},{},{},{},{},{},{},{},{},{}".format(u.id,u.usermail,u.password,p.id, p_tipo.nome, p_tipo.cnpj,m.id,m.rank,m.cnh,v.id,v.placa,v.cor,v.capacidade )
                    #retorna [14]
            else: return "login_deined,{}".format(login)





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
        else: return "login_deined"

@carropipa.route("/cadastro/cadastrarpedido", methods=["post","get"])
def cadastrar_pedido():
    if request.method == "POST":
        dataini = request.form.get("horaini")
        valor = request.form.get("valor")

        cliente_id = request.form.get("clienteid")

        pedido = Pedido(dataini, None, valor, cliente_id, None)

        db.session.add(pedido)
        db.session.commit()
        print(db.session)
        return "Cadastration_ok,"

@carropipa.route("/cadastro/finalizarpedido", methods=["post","get"])
def finalizar_pedido():
    if request.method == "POST":
        dataent = request.form.get("horaent")
        dataini = request.form.get("horaini")

        cliente_id = request.form.get("clienteid")
        motorista_id = request.form.get("motoristaid")
        pedido = Pedido.query.filter_by(cliente_id = cliente_id, hora_inicial = dataini).update(dict(motorista_id=motorista_id,hora_final=dataent))

        db.session.commit()
        print(db.session)
        return "Cadastration_ok,"



@carropipa.route("/login/getperfilMotorista", methods=["post","get"])
def getperfilMotorista():
    if request.method=='POST':
        p_tipo = True
        login,senha = request.form.get('email'), request.form.get('senha')
        u = User.query.filter_by(usermail=login).first()
        if u and u.password == senha:
            p = Pessoa.query.filter_by(user_id=u.id).first()
            m =  Motorista.query.filter_by(user_id=u.id).first()
            if p.tipo == "Pessoa Física":
                p_tipo = PessoaFisica.query.filter_by(pessoa_id=p.id).first()
                if p_tipo.cpf == '0' or p_tipo.telefone == 'x xxxx xxxx':
                    return "login_ok,{},{},{},{},{},{},{},{}".format(u.id,u.usermail,u.password,p.id, p_tipo.nome, p_tipo.sobrenome,m.id,m.rank,m.cnh )
                else:
                    return "login_ok,{},{},{},{},{},{},{},{},{},{}".format(u.id,u.usermail,u.password,p.id, p_tipo.nome, p_tipo.sobrenome,p_tipo.cpf,p_tipo.telefone,m.id,m.rank,m.cnh )
            else:
                #deveria ser juridica
                p_tipo = PessoaJuridica.query.filter_by(pessoa_id=p.id).first()
                if p_tipo.cnpj == '0':
                    return "login_ok,{},{},{},{},{},{},{}".format(u.id,u.usermail,u.password,p.id, p_tipo.nome,m.id,m.rank,m.cnh )
                else:
                    return "login_ok,{},{},{},{},{},{},{},{}".format(u.id,u.usermail,u.password,p.id, p_tipo.nome,p_tipo.cnpj,m.id,m.rank,m.cnh)
        else: return "login_deined"

@carropipa.route("/cadastro/criar_perfil_motorista", methods=["GET","POST"])
def criar_perfil_motorista():
    if request.method=="POST":
        login = request.form.get("usermail")
        senha = request.form.get('senha')
        print("login:",login)
       
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
                #pf = PessoaFisica.query.filter_by(pessoa_id = p.id).update(dict(cpf=cpf))
                #db.session.commit()
                e = Endereco.query.filter_by(pessoa_id = p.id).first()
                if e:
                    e = Endereco.query.filter_by(pessoa_id = p.id).update(dict(logradouro = logradouro, complemento = complemento, bairro = bairro, cep = cep, cidade = cidade, uf = uf))
                else:
                    e = Endereco(logradouro, complemento, bairro, cep, cidade, uf, p.id)
                    db.session.add(e)
                db.session.commit()
                print("Criado com sucesso")
                return "cadastration_ok,"
            else:
                print("pessoa não encontrado")
                return "cadastration_fail, pessoa não encontrado"
        else:
             print("usuario não encontrado")
             return "cadastration_fail, usuario não encontrado"
    else:
        return "cadastration_fail, metodo não permitido"
