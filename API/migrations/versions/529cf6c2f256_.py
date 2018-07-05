"""empty message

Revision ID: 529cf6c2f256
Revises: efb8cbc74df4
Create Date: 2018-06-18 23:09:04.073279

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '529cf6c2f256'
down_revision = 'efb8cbc74df4'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.create_table('enderecos',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('logradouro', sa.String(), nullable=True),
    sa.Column('complemento', sa.String(), nullable=True),
    sa.Column('bairro', sa.String(), nullable=True),
    sa.Column('cep', sa.String(), nullable=True),
    sa.Column('cidade', sa.String(), nullable=True),
    sa.Column('uf', sa.String(), nullable=True),
    sa.Column('pessoa_id', sa.Integer(), nullable=True),
    sa.ForeignKeyConstraint(['pessoa_id'], ['pessoas.id'], ),
    sa.PrimaryKeyConstraint('id')
    )
    op.add_column('pessoas', sa.Column('cpf', sa.String(), nullable=True))
    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.drop_column('pessoas', 'cpf')
    op.drop_table('enderecos')
    # ### end Alembic commands ###
