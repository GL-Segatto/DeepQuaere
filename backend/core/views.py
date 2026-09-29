from rest_framework import viewsets, status
from rest_framework.response import Response
from rest_framework.decorators import action
from rest_framework.permissions import AllowAny, IsAuthenticated
from django.db.models import Sum
from .models import Projeto, ItemOrcamento, Despesa
from .serializers import *

class UsuarioViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer
    permission_classes = [AllowAny]

class ProjetoViewSet(viewsets.ModelViewSet):
    queryset = Projeto.objects.all()
    serializer_class = ProjetoSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        return Projeto.objects.filter(usuarios=self.request.user)

    def perform_create(self, serializer):
        projeto = serializer.save()
        projeto.usuarios.add(self.request.user)

    @action(detail=True, methods=['get'])
    def dashboard(self, request, pk=None):
        projeto = self.get_object()

        total_orcado = ItemOrcamento.objects.filter(projeto=projeto).aggregate(Sum('valor_planejado'))['valor_planejado__sum'] or 0.00
        total_realizado = Despesa.objects.filter(projeto=projeto).aggregate(Sum('valor'))['valor__sum'] or 0.00
        saldo_final = float(total_orcado) - float(total_realizado)
        
        percentual_gasto = (float(total_realizado) / float(total_orcado) * 100) if total_orcado > 0 else 0.00

        despesas_custeio = Despesa.objects.filter(projeto=projeto, tipo_despesa='CUSTEIO').aggregate(Sum('valor'))['valor__sum'] or 0.00
        despesas_capital = Despesa.objects.filter(projeto=projeto, tipo_despesa='CAPITAL').aggregate(Sum('valor'))['valor__sum'] or 0.00

        return Response({
            'id_projeto': projeto.id_projeto,
            'nome_projeto': projeto.nome,
            'total_orcado': float(total_orcado),
            'total_realizado': float(total_realizado),
            'saldo_final': saldo_final,
            'percentual_gasto': round(percentual_gasto, 2),
            'resumo_custeio': float(despesas_custeio),
            'resumo_capital': float(despesas_capital)
        })

class ItemOrcamentoViewSet(viewsets.ModelViewSet):
    queryset = ItemOrcamento.objects.all()
    serializer_class = ItemOrcamentoSerializer

    def get_queryset(self):
        queryset = ItemOrcamento.objects.all()
        
        projeto_id = self.request.query_params.get('projeto')
        if projeto_id is not None:
            queryset = queryset.filter(projeto_id=projeto_id)
            
        return queryset

class DespesaViewSet(viewsets.ModelViewSet):
    queryset = Despesa.objects.all()
    serializer_class = DespesaSerializer
    permission_classes = [IsAuthenticated]
