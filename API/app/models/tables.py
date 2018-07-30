from app import db

class User(db.Model):

    __tablename__ = 'users'
    id = db.Column(db.Integer, primary_key=True)
    usermail = db.Column(db.String, unique=True)
    password = db.Column(db.String)


    @property
    def is_authenticated(self):
        return True

    @property
    def is_active(self):
        return True

    @property
    def is_anonymous(self):
        return False

    def get_id(self):
        return self.id

    def __init__(self, usermail, password):
        self.usermail = usermail
        self.password = password


    def __repr__(self):
        return '<User %r>' % self.usermail


class Pessoa(db.Model):

    __tablename__ = 'pessoas'
    id = db.Column(db.Integer, primary_key=True)
    tipo = db.Column(db.String)
    user_id = db.Column(db.Integer, db.ForeignKey('users.id'))
    

    user = db.relationship('User', foreign_keys=user_id)


    def __init__(self,tipo, user_id):
        self.tipo = tipo
        self.user_id = user_id

    def __repr__(self):
        return '<Tipo de Pessoa: %r \n User id: %r>' % (self.tipo, self.user_id)

class PessoaFisica(db.Model):

    __tablename__ = 'pessoasfisicas'
    id = db.Column(db.Integer, primary_key=True)
    cpf = db.Column(db.String, default='0')
    nome = db.Column(db.String)
    sobrenome = db.Column(db.String)
    telefone = db.Column(db.String, default='x xxxx xxxx')

    pessoa_id = db.Column(db.Integer, db.ForeignKey('pessoas.id'))

    pessoa = db.relationship('Pessoa', foreign_keys=pessoa_id)

    def __init__(self, cpf, nome, sobrenome, pessoa_id,telefone):
        self.cpf = cpf
        self.nome = nome
        self.sobrenome = sobrenome
        self.pessoa_id = pessoa_id
        self.telefone = telefone


    def __repr__(self):
        return '<Pessoa: %r %r pessoa id: %r telefone: %r>' % (self.nome, self.sobrenome, self.pessoa_id, self.telefone)


class PessoaJuridica(db.Model):

    __tablename__ = 'pessoasjuridicas'
    id = db.Column(db.Integer, primary_key=True)
    cnpj = db.Column(db.String, default = '0')
    nome = db.Column(db.String)

    pessoa_id = db.Column(db.Integer, db.ForeignKey('pessoas.id'))

    pessoa = db.relationship('Pessoa', foreign_keys=pessoa_id)

    def __init__(self, cnpj, nome, pessoa_id):
        self.cnpj = cnpj
        self.nome = nome
        self.pessoa_id = pessoa_id


    def __repr__(self):
        return '<Empresa: %r %r pessoa id: %r>' % (self.nome, self.cnpj, self.pessoa_id)


class Motorista(db.Model):

    __tablename__ = 'motoristas'
    id = db.Column(db.Integer, primary_key=True)
    cnh = db.Column(db.String, default = '0')
    entregas = db.Column(db.String)
    rank = db.Column(db.Float, default = 0.0)

    pessoa_id = db.Column(db.Integer, db.ForeignKey('pessoas.id'))
    user_id = db.Column(db.Integer, db.ForeignKey('users.id'))

    pessoa = db.relationship('Pessoa', foreign_keys=pessoa_id)
    usuario = db.relationship('User', foreign_keys=user_id)

    def __init__(self, cnh, entregas, rank, user_id, pessoa_id):
        self.cnh = cnh
        self.entregas = entregas
        self.rank = rank
        self.user_id = user_id
        self.pessoa_id = pessoa_id


    def __repr__(self):
        return '<Motorista: %r cnh: %r>' % (self.pessoa_id.nome, self.cnh)


class Cliente(db.Model):

    __tablename__ = 'clientes'
    id = db.Column(db.Integer, primary_key=True)
    carteira = db.Column(db.String, default='0')
    rank = db.Column(db.Float, default=0.0)

    pessoa_id = db.Column(db.Integer, db.ForeignKey('pessoas.id'))
    user_id = db.Column(db.Integer, db.ForeignKey('users.id'))

    pessoa = db.relationship('Pessoa', foreign_keys=pessoa_id)
    usuario = db.relationship('User', foreign_keys=user_id)

    def __init__(self, carteira, rank, user_id, pessoa_id):
        self.carteira = carteira
        self.rank = rank
        self.user_id = user_id
        self.pessoa_id = pessoa_id


    def __repr__(self):
        return '<Cliente: %r %r rank: %r>' % (self.pessoa_id.nome, self.pessoa_id.sobrenome, self.rank)


class Avaliacao(db.Model):

    __tablename__ = 'avaliacoes'
    id = db.Column(db.Integer, primary_key=True)
    nota_motorista = db.Column(db.Integer)
    nota_cliente = db.Column(db.Integer)

    cliente_id = db.Column(db.Integer, db.ForeignKey('clientes.id'))
    motorista_id = db.Column(db.Integer, db.ForeignKey('motoristas.id'))

    cliente = db.relationship('Cliente', foreign_keys=cliente_id)
    motorista = db.relationship('Motorista', foreign_keys=motorista_id)

    def __init__(self, nota_motorista, nota_cliente, cliente_id, motorista_id):
        self.nota_motorista = nota_motorista
        self.nota_cliente = nota_cliente
        self.cliente_id = cliente_id
        self.motorista_id = motorista_id


class Veiculo(db.Model):
    __tablename__ = 'veiculos'
    id = db.Column(db.Integer, primary_key=True)
    placa = db.Column(db.String)
    capacidade = db.Column(db.String)
    cor = db.Column(db.String)

    motorista_id = db.Column(db.Integer, db.ForeignKey('motoristas.id'))

    motorista = db.relationship('Motorista', foreign_keys=motorista_id)

    def __init__(self, placa, capacidade, cor, motorista_id):
        self.placa = placa
        self.capacidade = capacidade
        self.cor = cor
        self.motorista_id = motorista_id

    def __repr__(self):
        return '<Veiculo placa: %r  capacidade: %r cor: %r>' % (self.placa, self.capacidade, self.cor)


class Pedido(db.Model):
    __tablename__ = 'pedidos'
    id = db.Column(db.Integer, primary_key=True)
    hora_inicial = db.Column(db.String)
    hora_final = db.Column(db.String)
    valor = db.Column(db.Float)

    cliente_id = db.Column(db.Integer, db.ForeignKey('clientes.id'))
    motorista_id = db.Column(db.Integer, db.ForeignKey('motoristas.id'))

    cliente = db.relationship('Cliente', foreign_keys=cliente_id)
    motorista = db.relationship('Motorista', foreign_keys=motorista_id)

    def __init__(self, hora_inicial, hora_final, valor, cliente_id, motorista_id):
        self.hora_inicial = hora_inicial
        self.hora_final = hora_final
        self.valor = valor
        self.cliente_id = cliente_id
        self.motorista_id = motorista_id


class Pagamento(db.Model):
    __tablename__ = 'pagamentos'
    id = db.Column(db.Integer, primary_key=True)
    descricao = db.Column(db.String)

    pedido_id = db.Column(db.Integer, db.ForeignKey('pedidos.id'))

    pedido = db.relationship('Pedido', foreign_keys=pedido_id)

    def __init__(self, descricao, pedido_id):
        self.descricao = descricao
        self.pedido_id = pedido_id


class Endereco(db.Model):
    __tablename__ = 'endereco'

    id = db.Column(db.Integer, primary_key=True)
    logradouro = db.Column(db.String)
    complemento = db.Column(db.String)
    bairro = db.Column(db.String)
    cep = db.Column(db.Integer)
    cidade = db.Column(db.String)
    uf = db.Column(db.String)
    pessoa_id = db.Column(db.Integer, db.ForeignKey('pessoas.id'))

    pessoa = db.relationship('Pessoa', foreign_keys=pessoa_id)

    def __init__(self, logradouro, complemento, bairro, cep, cidade, uf, pessoa_id):
        self.logradouro = logradouro
        self.complemento = complemento
        self.bairro = bairro
        self.cep =cep
        self.cidade =cidade
        self.uf =uf
        self.pessoa_id = pessoa_id
    
