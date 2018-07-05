"""empty message

Revision ID: 2aabb16ab0a3
Revises: 529cf6c2f256
Create Date: 2018-06-22 23:57:49.120728

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '2aabb16ab0a3'
down_revision = '529cf6c2f256'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.create_table('clientes',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('carteira', sa.String(), nullable=True),
    sa.Column('rank', sa.Float(), nullable=True),
    sa.Column('pessoa_id', sa.Integer(), nullable=True),
    sa.Column('user_id', sa.Integer(), nullable=True),
    sa.ForeignKeyConstraint(['pessoa_id'], ['pessoas.id'], ),
    sa.ForeignKeyConstraint(['user_id'], ['users.id'], ),
    sa.PrimaryKeyConstraint('id')
    )
    op.create_table('motoristas',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('cnh', sa.String(), nullable=True),
    sa.Column('entregas', sa.String(), nullable=True),
    sa.Column('rank', sa.Float(), nullable=True),
    sa.Column('pessoa_id', sa.Integer(), nullable=True),
    sa.Column('user_id', sa.Integer(), nullable=True),
    sa.ForeignKeyConstraint(['pessoa_id'], ['pessoas.id'], ),
    sa.ForeignKeyConstraint(['user_id'], ['users.id'], ),
    sa.PrimaryKeyConstraint('id')
    )
    op.create_table('pessoasfisicas',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('cpf', sa.String(), nullable=True),
    sa.Column('nome', sa.String(), nullable=True),
    sa.Column('sobrenome', sa.String(), nullable=True),
    sa.Column('pessoa_id', sa.Integer(), nullable=True),
    sa.ForeignKeyConstraint(['pessoa_id'], ['pessoas.id'], ),
    sa.PrimaryKeyConstraint('id')
    )
    op.create_table('pessoasjuridicas',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('cnpj', sa.String(), nullable=True),
    sa.Column('nome', sa.String(), nullable=True),
    sa.Column('pessoa_id', sa.Integer(), nullable=True),
    sa.ForeignKeyConstraint(['pessoa_id'], ['pessoas.id'], ),
    sa.PrimaryKeyConstraint('id')
    )
    op.create_table('avaliacoes',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('nota_motorista', sa.Integer(), nullable=True),
    sa.Column('nota_cliente', sa.Integer(), nullable=True),
    sa.Column('cliente_id', sa.Integer(), nullable=True),
    sa.Column('motorista_id', sa.Integer(), nullable=True),
    sa.ForeignKeyConstraint(['cliente_id'], ['clientes.id'], ),
    sa.ForeignKeyConstraint(['motorista_id'], ['motoristas.id'], ),
    sa.PrimaryKeyConstraint('id')
    )
    op.create_table('pedidos',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('hora_inicial', sa.String(), nullable=True),
    sa.Column('hora_final', sa.String(), nullable=True),
    sa.Column('valor', sa.Float(), nullable=True),
    sa.Column('cliente_id', sa.Integer(), nullable=True),
    sa.Column('motorista_id', sa.Integer(), nullable=True),
    sa.ForeignKeyConstraint(['cliente_id'], ['clientes.id'], ),
    sa.ForeignKeyConstraint(['motorista_id'], ['motoristas.id'], ),
    sa.PrimaryKeyConstraint('id')
    )
    op.create_table('veiculos',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('placa', sa.String(), nullable=True),
    sa.Column('capacidade', sa.String(), nullable=True),
    sa.Column('cor', sa.String(), nullable=True),
    sa.Column('motorista_id', sa.Integer(), nullable=True),
    sa.ForeignKeyConstraint(['motorista_id'], ['motoristas.id'], ),
    sa.PrimaryKeyConstraint('id')
    )
    op.create_table('pagamentos',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('descricao', sa.String(), nullable=True),
    sa.Column('pedido_id', sa.Integer(), nullable=True),
    sa.ForeignKeyConstraint(['pedido_id'], ['pedidos.id'], ),
    sa.PrimaryKeyConstraint('id')
    )
    op.drop_table('enderecos')
    op.add_column('pessoas', sa.Column('tipo', sa.String(), nullable=True))
    op.drop_column('pessoas', 'telefone')
    op.drop_column('pessoas', 'rank')
    op.drop_column('pessoas', 'lastname')
    op.drop_column('pessoas', 'cpf')
    op.drop_column('pessoas', 'name')
    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.add_column('pessoas', sa.Column('name', sa.VARCHAR(), nullable=True))
    op.add_column('pessoas', sa.Column('cpf', sa.VARCHAR(), nullable=True))
    op.add_column('pessoas', sa.Column('lastname', sa.VARCHAR(), nullable=True))
    op.add_column('pessoas', sa.Column('rank', sa.FLOAT(), nullable=True))
    op.add_column('pessoas', sa.Column('telefone', sa.VARCHAR(), nullable=True))
    op.drop_column('pessoas', 'tipo')
    op.create_table('enderecos',
    sa.Column('id', sa.INTEGER(), nullable=False),
    sa.Column('logradouro', sa.VARCHAR(), nullable=True),
    sa.Column('complemento', sa.VARCHAR(), nullable=True),
    sa.Column('bairro', sa.VARCHAR(), nullable=True),
    sa.Column('cep', sa.VARCHAR(), nullable=True),
    sa.Column('cidade', sa.VARCHAR(), nullable=True),
    sa.Column('uf', sa.VARCHAR(), nullable=True),
    sa.Column('pessoa_id', sa.INTEGER(), nullable=True),
    sa.ForeignKeyConstraint(['pessoa_id'], ['pessoas.id'], ),
    sa.PrimaryKeyConstraint('id')
    )
    op.drop_table('pagamentos')
    op.drop_table('veiculos')
    op.drop_table('pedidos')
    op.drop_table('avaliacoes')
    op.drop_table('pessoasjuridicas')
    op.drop_table('pessoasfisicas')
    op.drop_table('motoristas')
    op.drop_table('clientes')
    # ### end Alembic commands ###
