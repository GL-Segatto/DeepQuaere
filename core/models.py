from django.db import models
from django.contrib.auth.models import User

class Projeto(models.Model):
    id_projeto = models.AutoField(primary_key=True)
    nome = models.CharField(max_length=100)
    descricao = models.TextField(blank=True, null=True)
    data_inicio = models.DateField()
    data_fim = models.DateField()
    usuarios = models.ManyToManyField(User, related_name='projetos')

    def __str__(self):
        return self.nome

class ItemOrcamento(models.Model):
    id_item_orcamento = models.AutoField(primary_key=True)
    descricao = models.CharField(max_length=100)
    valor_planejado = models.DecimalField(max_digits=12, decimal_places=2)
    categoria = models.CharField(max_length=50)
    projeto = models.ForeignKey(Projeto, on_delete=models.CASCADE, related_name='itens_orcamento')

class Despesa(models.Model):
    TIPO_CHOICES = [
        ('CUSTEIO', 'Custeio'),
        ('CAPITAL', 'Capital'),
    ]
    id_despesa = models.AutoField(primary_key=True)
    descricao = models.CharField(max_length=100)
    valor = models.DecimalField(max_digits=12, decimal_places=2)
    data_despesa = models.DateField()
    tipo_despesa = models.CharField(max_length=10, choices=TIPO_CHOICES)
    projeto = models.ForeignKey(Projeto, on_delete=models.CASCADE, related_name='despesas')
    item_orcamento = models.ForeignKey(ItemOrcamento, on_delete=models.SET_NULL, null=True, blank=True)
