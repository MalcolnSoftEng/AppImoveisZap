# AppImoveisZap
App tipo Imobiliária Imoveis ZAP - Android Java nativo desenvolvido com MVC - Clean Architecture 
Versão SDK 26 , VErsão Mínima Android 15 Camadas e disposições de "classes"/pastas:
*controller 
  ImovelAdapter - classe herdando PagerAdapter 
  ListaAdapter - classe herdando RecyclerView Adapter implementando interface Filterable (Filtro) 
*model 
  Filtro - Atributos: valor, quantDormitorios, suites,vagas 
  Imoveis - Atributos para Integração Http/Json: CodigoImovel, TipoImovel, Endereço, etc 
  Usuario - Atributos do usuário Integração POST/Json: nome,email,telefone,etc 
*util 
  IsOnline - classe utilitária verificando status de conexão Utiliza classe Android "ConnectivityManager" 
  MalcolnGeoPoint - classe própria efetua Calculos GEOLOCALIZAÇÃO E CORRDENADAS convertendo distancia LINEAR em KM
*webservice 
  GeocodeHttp - classe Http retornando status Json e calculando distancias de Imoveis ao ponto do Usuário, utilização da API geocode Google 
  ImoveisHttp - Http/Json lista imoveis da resposta Json mensagemHttp - Envia Post Json sobre interesse no imovel 
  
CLASSES View - utilização suportDesign, CardView, ButterKnife, OkHttp, googlePlayServicesLocation, Picasso 
  ImovelDetalhesActivity 
  ListWebServiceFragment 
  MAinActivity
