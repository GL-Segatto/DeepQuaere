from django.urls import path, include
from rest_framework.routers import DefaultRouter
from rest_framework_nested import routers
from .views import UsuarioViewSet, ProjetoViewSet, ItemOrcamentoViewSet, DespesaViewSet

router = DefaultRouter()
router.register(r'usuarios', UsuarioViewSet, basename='usuario')
router.register(r'projetos', ProjetoViewSet, basename='projeto')
router.register(r'despesas', DespesaViewSet, basename='despesa')

projetos_router = routers.NestedDefaultRouter(router, r'projetos', lookup='projeto')
projetos_router.register(r'itens_orcamento', ItemOrcamentoViewSet, basename='projeto-itens-orcamento')

urlpatterns = [
    path('', include(router.urls)),
    path('', include(projetos_router.urls)),
]
