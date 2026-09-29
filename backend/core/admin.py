from django.contrib import admin
from .models import Projeto, ItemOrcamento

@admin.register(Projeto)
class ProjetoAdmin(admin.ModelAdmin):
    list_display = ('__str__', 'data_inicio', 'data_fim')
    search_fields = ('nome',)

@admin.register(ItemOrcamento)
class ItemOrcamentoAdmin(admin.ModelAdmin):
    list_display = ('descricao', 'categoria', 'projeto')
    list_filter = ('categoria',)
    search_fields = ('descricao', 'categoria')
