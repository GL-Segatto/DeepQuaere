from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import UsuarioViewSet, ProjetoViewSet, ItemOrcamentoViewSet, DespesaViewSet

router = DefaultRouter()
router.register(r'usuarios', UsuarioViewSet, basename='usuario')
router.register(r'projetos', ProjetoViewSet, basename='projeto')
router.register(r'itens-orcamento', ItemOrcamentoViewSet, basename='item-orcamento')
router.register(r'despesas', DespesaViewSet, basename='despesa')

urlpatterns = [
    path('', include(router.urls)),
]
