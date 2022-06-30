# "Recortes e Loops: Colagem Musical e Síntese Granular Dentro do Supercollider"
Projeto final da cadeira de Programação para música ministrada por Henrique Vaz na pós-graduação de Produção Sonora expandia da UNIAESO. Posto aqui, pois foi meu primeiro contato com códigos. Utilizamos o Supercollider, uma linguagem e ambiente de programação voltado para criação musical, através de síntese sonora. 

### Introdução

Ao longo da disciplina Programação Para Música, desenvolvemos conhecimentos acerca dos padrões sintáticos e estruturais do Supercollider, uma linguagem e ambiente de desenvolvimento musical que se utiliza de códigos de programação para a construção, manipulação e reprodução sonora.  

Foram demonstrados padrões e formas de desenvolver sintetizadores digitais, ondas sonoras, dentre outras ferramentas, sempre com a possibilidade de controlá-las através de interfaces gráficas. 

Ao final do processo, foi proposta a composição de um tema com os conhecimentos adquiridos, podendo o aluno optar por desenvolver um código do zero ou utilizar as aplicações demonstradas nas aulas. 

### Meu Projeto

Decidi por remixar códigos demonstrados durante o período da disciplina e buscar outros em fóruns como o sccode.org. Além disso, busquei loops em wav para utilizar na síntese. A ideia era fazer um paralelo entre a composição no Supercollider e o modo com que a música é feita contemporaneamente, com samples e afins. Nomeei o projeto de "Recortes e Loops: Colagem Musical e Síntese Granular Dentro do Supercollider" e ao fim do processo, saí com três composições que explicarei nos próximos tópicos.

Sem compromisso tonal ou estético, fui por três linhas. Todas utilizando como base um sintetizador granular apresentado na última semana de aula. Nele, é possível direcionar uma pasta onde estarão arquivos de áudio e manipular os seguintes parâmetros: [A] Carga do Buffer / Mixagem; [B] Varredura Através de Buffers; [C] Duração; [D] Escalonamento do Grão; [E] Sobreposição, [F] Deslocamento; [G] Densidade; [H] Amplitude e [I] Dispersão no Campo.

Segue o código do sintetizador granular. Na primeira parte a localização da pasta onde se encontram os samples, na segunda e terceira parte a estrutura sonora do mesmo e na quarta parte as definições da interface gráfica: 

///   1   ///
(~folder="/Users/tulioalbu/Music/SuperCollider Recordings/teste";
~buffers.do{|x| x.free};
~buffers= (~folder+/+"*").pathMatch.collect{|x| Buffer.readChannel(s, x, channels:[0])};)

///   2   ///
(SynthDef(\grao, {|out=0,buf,rate=1,offset=0,time=0.1, amp=0.1,pan=0|
var env= EnvGen.ar(Env.sine(time), doneAction:2);
var src= PlayBuf.ar(1,buf,rate*BufRateScale.ir(buf),1,offset*BufFrames.ir(buf),1);
OffsetOut.ar(out, Pan2.ar(src*env*amp, pan));}).add;)
///   3   ///
(Pdef(\granulador, Pbind(\instrument, \grao, //-> definições: \buf,Pwrand(~buffers, Pdefn(\weights, 1.dup(~buffers.size).normalizeSum),inf), \dur, Pdefn(\dur, 0.125/8), \time, Pkey(\dur)*Pdefn(\overlap, 2,5), \rate, Pdefn(\rate, Pwhite(1, 1, inf)), \offset, Pdefn(\offset, Pwhite(0.25, 0.25, inf)), \amp, 0.5*Pdefn(\amp,Pwhite(1, 1, inf))*Pwrand([1,0],Pdefn(\density, [1,0]), inf), \pan, Pgauss(0, Pdefn(\pan,0.25)),)).play;)

///   4   ///
(var win= Window("SÍNTESE GRANULAR", Rect(10, 10, 400, 600)).background_(Color.grey);var mul;win.layout= VLayout(
StaticText().string_("[A] carga do buffer / mixagem:"),
mul= MultiSliderView().value_(Pdefn(\weights).source).elasticMode_(1)
.action_({|v| Pdefn(\weights, v.value.normalizeSum)}).colors_(Color.green, Color.green(0.5)),
StaticText().string_("[B] varredura através de buffers:"),
Slider().orientation_(\horizontal)
.action_({|v| mul.valueAction_(({|i| abs(i/(~buffers.size-1)-v.value).max(0.001)** -2}!~buffers.size).normalizeSum)}),
StaticText().string_("[C] dur.:"),
Slider().orientation_(\horizontal).value_(Pdefn(\dur).source*10)
.action= {|v| Pdefn(\dur, v.value*0.1+0.001)},
StaticText().string_("[D] escalonamento do grão:"),
RangeSlider().orientation_(\horizontal).lo_(0.5).hi_(0.5)
.action= {|v| Pdefn(\rate, Pwhite(v.lo*2, v.hi*2, inf))},
StaticText().string_("[E] sobreposição:"),	Slider().orientation_(\horizontal).value_(Pdefn(\overlap).source*0.05)
.action= {|v| Pdefn(\overlap, v.value*20)},
StaticText().string_("[F] deslocamento:"),
RangeSlider().orientation_(\horizontal).lo_(0.25).hi_(0.5)
.action= {|v| Pdefn(\offset, Pwhite(v.lo, v.hi, inf))},
StaticText().string_("[G] densidade:"),
Slider().orientation_(\horizontal).value_(1)
.action= {|v| Pdefn(\density, [v.value, 1-v.value])},
StaticText().string_("[H] amplitude:"),
RangeSlider().orientation_(\horizontal).lo_(1).hi_(1)
.action= {|v| Pdefn(\amp, Pwhite(v.lo, v.hi, inf))},
StaticText().string_("[I] dispersão no campo:"),
Slider().orientation_(\horizontal).value_(0.25)
.action= {|v| Pdefn(\pan, v.value)}
);CmdPeriod.doOnce({win.close});win.front;))



### As composições

Composição 1 - "Granular Weeknd Arpeggiator"

[ESCUTE AQUI](https://drive.google.com/file/d/15P5ARAOaVM9j_6rif9Dlb6akRiwKL2JO/view)

Essa composição usou basicamente três códigos: o já citado e demonstrado sintetizador granular, um Splay demonstrado na terceira semana de aula e uma música de Nathaniel Virgo, presente na coletânea sc14o, onde vários compositores ao redor do mundo foram desafiados a montar códigos no Supercollider com até 140 caracteres (número limite do Twitter). 

No sintetizador granular utilizei apenas dois samples, inspirado em um achado no github. Lá encontrei o código para SC da música "The Hills" da banda "The Weeknd". Coloquei o código no Supercollider, gravei, e transformei em AIFF. Também baixei a versão original e coloquei os dois na pasta de Teste para utilização na síntese. 

O começo da faixa feita por mim é basicamente uma brincadeira com os parâmetros do granular. Depois de 22 segundos, vão entrando gradualmente os 3 Splays, setados de formas diferentes. Eis aqui os códigos desses loops, que lembram arpeggiators: 

Loop 1
(a = {|fundamental = 50| //arg. ou argumentação para ser reatribuída por ".set".
var harmonicos = [1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12];
var gerador_sonoro= BPF.ar(
in: Saw.ar(freq: 8, mul: LFPulse.kr(harmonicos, width: 0.1)),//entrada do filtro.
freq: harmonicos * fundamental,//frequência central do filtro.
rq: 0.01,
mul: 40);
	Splay.ar(gerador_sonoro);//mistura em estéreo
}.play;)

Loop 2
(a = {|fundamental = 500| //arg. ou argumentação para ser reatribuída por ".set".
var harmonicos = [1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12];
var gerador_sonoro= BPF.ar(
in: Saw.ar(freq: 8, mul: LFPulse.kr(harmonicos, width: 0.1)),//entrada do filtro.
freq: harmonicos * fundamental,//frequência central do filtro.
rq: 0.01,
mul: 40);
	Splay.ar(gerador_sonoro);//mistura em estéreo}.play;)
Loop 3
(a = {|fundamental = 203| //arg. ou argumentação para ser reatribuída por ".set".
var harmonicos = [1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12];
var gerador_sonoro= BPF.ar(
in: Saw.ar(freq: 8, mul: LFPulse.kr(harmonicos, width: 0.1)),//entrada do filtro.
freq: harmonicos * fundamental,//frequência central do filtro.
rq: 0.01,
mul: 40);
	Splay.ar(gerador_sonoro);//mistura em estéreo
}.play;)

Depois da entrada dos 3 loops, é a vez da chegada da composição de Nathaniel Virgo, que é basicamente uma estrutura de noises que lembram por vezes trovões ou simulações de vinil. Eis o código:

Música de Nathaniel Virgo
play{p=PinkNoise.ar(1!2);BRF.ar(p+Blip.ar(p+2,400),150,2,0.1)+LPF.ar(FreeVerb2.ar(*LPF.ar(p+0.2*Dust.ar(0.1),60)++[1,1,0.2,1e4]).tanh,1000)}

A faixa foi finalizada no Pro Tools onde fiz os cortes e adicionei Reverb. A faixa sem processamento pode ser ouvida AQUI.

Composição 2 - "Silent Hill Sax"

[ESCUTE AQUI](https://drive.google.com/file/d/1wWG56bC9OVx2zGEhLm3UX4rlNtZuE_Z9/view)

Construída exclusivamente com a aplicação do arpeggiator, a faixa foi feita a partir de 5 samples baixados do site looperman.com. Todos eles com simulações ou alguém tocando saxofone. Segue link com os cinco samples:

[OUÇA OS LOOPS AQUI](https://drive.google.com/drive/u/5/folders/1NLt8jwgj3-2EH25kGfLjkbtjH2TE67l3)

Da mesma forma que a primeira música, a grande coisa aqui foi trabalhar com os parâmetros do Granulador, construindo camadas e espacialidade. A inspiração foi a trilha de Silent Hill, jogo clássico de terror do Playstation. Ao ouvir com fones ou com monitores estéreo, é possível perceber a movimentação entre as camadas sonoras e a interação no panorama, causando um intencional desconforto.

A finalização e corte foi feita também no Pro Tools, onde foram adicionados plugins de Delay e Reverb. 

Composição 3 - "Epic Sax Guy Conhece Silent Hill"

[ESCUTE AQUI](https://drive.google.com/file/d/1UsETf-teDrxUoca08jF4YSVhSt8kFwwo/view)

Por último, uma brincadeira. Durante minha pesquisa pelos códigos nos fóruns, me deparei com uma versão para Supercollider da clássica música do meme "Epic Sax Guy". Isso que me motivou a procurar pelos samples de Saxofone e eventualmente compor a faixa anterior. De toda forma, não quis desperdiçar esse achado e juntei o contexto da faixa sombria com essa. Um resultado, no mínimo, inusitado. 



