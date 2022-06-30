/// Música 1 ///
/// Códigos Utilizados e citados no projeto ///

///Granulador///

(
~folder="/Users/tulioalbu/Music/SuperCollider Recordings/teste";
~buffers.do{|x| x.free};
~buffers= (~folder+/+"*").pathMatch.collect{|x| Buffer.readChannel(s, x, channels:[0])};
)

(
SynthDef(\grao, {|out=0,buf,rate=1,offset=0,time=0.1, amp=0.1,pan=0|
var env= EnvGen.ar(Env.sine(time), doneAction:2);
var src= PlayBuf.ar(1,buf,rate*BufRateScale.ir(buf),1,offset*BufFrames.ir(buf),1);
OffsetOut.ar(out, Pan2.ar(src*env*amp, pan));
}).add;
)

(
Pdef(\granulador,
Pbind(\instrument,
\grao,
//-> definições:
\buf,Pwrand(~buffers, Pdefn(\weights, 1.dup(~buffers.size).normalizeSum),inf),
\dur, Pdefn(\dur, 0.125/8),
\time, Pkey(\dur)*Pdefn(\overlap, 2,5),
\rate, Pdefn(\rate, Pwhite(1, 1, inf)),
\offset, Pdefn(\offset, Pwhite(0.25, 0.25, inf)),
\amp, 0.5*Pdefn(\amp,Pwhite(1, 1, inf))*Pwrand([1,0],Pdefn(\density, [1,0]), inf),
\pan, Pgauss(0, Pdefn(\pan,0.25)),
)).play;
)

//->interface:

(
var win= Window("SÍNTESE GRANULAR", Rect(10, 10, 400, 600)).background_(Color.grey);
var mul;
win.layout= VLayout(
StaticText().string_("[A] carga do buffer / mixagem:"),
mul= MultiSliderView().value_(Pdefn(\weights).source).elasticMode_(1)
.action_({|v| Pdefn(\weights, v.value.normalizeSum)}).colors_(Color.green, Color.green(0.5)),

/////////////////////////////////
StaticText().string_("[B] varredura através de buffers:"),
Slider().orientation_(\horizontal)
.action_({|v| mul.valueAction_(({|i| abs(i/(~buffers.size-1)-v.value).max(0.001)** -2}!~buffers.size).normalizeSum)}),

/////////////////////////////////
StaticText().string_("[C] dur.:"),
Slider().orientation_(\horizontal).value_(Pdefn(\dur).source*10)
.action= {|v| Pdefn(\dur, v.value*0.1+0.001)},

/////////////////////////////////
StaticText().string_("[D] escalonamento do grão:"),
RangeSlider().orientation_(\horizontal).lo_(0.5).hi_(0.5)
.action= {|v| Pdefn(\rate, Pwhite(v.lo*2, v.hi*2, inf))},

/////////////////////////////////
StaticText().string_("[E] sobreposição:"),	Slider().orientation_(\horizontal).value_(Pdefn(\overlap).source*0.05)
.action= {|v| Pdefn(\overlap, v.value*20)},

/////////////////////////////////
StaticText().string_("[F] deslocamento:"),
RangeSlider().orientation_(\horizontal).lo_(0.25).hi_(0.5)
.action= {|v| Pdefn(\offset, Pwhite(v.lo, v.hi, inf))},

/////////////////////////////////
StaticText().string_("[G] densidade:"),
Slider().orientation_(\horizontal).value_(1)
.action= {|v| Pdefn(\density, [v.value, 1-v.value])},

/////////////////////////////////
StaticText().string_("[H] amplitude:"),
RangeSlider().orientation_(\horizontal).lo_(1).hi_(1)
.action= {|v| Pdefn(\amp, Pwhite(v.lo, v.hi, inf))},

/////////////////////////////////
StaticText().string_("[I] dispersão no campo:"),
Slider().orientation_(\horizontal).value_(0.25)
.action= {|v| Pdefn(\pan, v.value)}
);
CmdPeriod.doOnce({win.close});
win.front;
)
)

///


/// Música de Nathaniel Virgo ///
play{p=PinkNoise.ar(1!2);BRF.ar(p+Blip.ar(p+2,400),150,2,0.1)+LPF.ar(FreeVerb2.ar(*LPF.ar(p+0.2*Dust.ar(0.1),60)++[1,1,0.2,1e4]).tanh,1000)}


//// loop 1 ///
(
a = {|fundamental = 50| //arg. ou argumentação para ser reatribuída por ".set".
var harmonicos = [1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12];
var gerador_sonoro= BPF.ar(
in: Saw.ar(freq: 8, mul: LFPulse.kr(harmonicos, width: 0.1)),//entrada do filtro.
freq: harmonicos * fundamental,//frequência central do filtro.
rq: 0.01,
mul: 40);
	Splay.ar(gerador_sonoro);//mistura em estéreo
}.play;
)
//// loop 2 ///
(
a = {|fundamental = 500| //arg. ou argumentação para ser reatribuída por ".set".
var harmonicos = [1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12];
var gerador_sonoro= BPF.ar(
in: Saw.ar(freq: 8, mul: LFPulse.kr(harmonicos, width: 0.1)),//entrada do filtro.
freq: harmonicos * fundamental,//frequência central do filtro.
rq: 0.01,
mul: 40);
	Splay.ar(gerador_sonoro);//mistura em estéreo
}.play;
)

/// loop 3 ///
(
a = {|fundamental = 203| //arg. ou argumentação para ser reatribuída por ".set".
var harmonicos = [1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12];
var gerador_sonoro= BPF.ar(
in: Saw.ar(freq: 8, mul: LFPulse.kr(harmonicos, width: 0.1)),//entrada do filtro.
freq: harmonicos * fundamental,//frequência central do filtro.
rq: 0.01,
mul: 40);
	Splay.ar(gerador_sonoro);//mistura em estéreo
}.play;
)


/// Música de The Weeknd ///


///////////////////////////////////////
///////  RUN THE SYNTH DEFS  //////////
///////////////////////////////////////

(
SynthDef("hihat", {arg out = 0, amp = 0.5, att = 0.01, rel = 0.2, ffreq = 12000, pan = 0;
	var env, snd;
	env =  Env.perc(
		attackTime: att,
		releaseTime: rel,
		level: amp).kr(doneAction: 2);
	snd =  WhiteNoise.ar(
		mul: env,
		add: 0);
	snd = LPF.ar(
		in: snd,
		freq: ffreq,
		mul: 1,
		add: 0);
	Out.ar(out, Pan2.ar(snd, pan));
}).add;

SynthDef("kick", {arg out = 0, amp = 0.5, sinfreq = 60, glissf = 0.9, att = 0.01, rel = 0.45, pan = 0;
	var env, snd, ramp;
	env =  Env.perc(
		attackTime: att,
		releaseTime: rel,
		level: amp).kr(doneAction: 2);
	ramp = XLine.kr(
		start: sinfreq,
		end: sinfreq * glissf,
		dur: rel
	);
	snd = SinOsc.ar(
		freq: ramp,
		phase: 0,
		mul: env,
		add: 0);
	snd = Pan2.ar(snd, pan);
	Out.ar(out, snd);
}).add;

SynthDef("plucking", {arg amp = 0.1, freq = 440, decay = 5, dampen = 0.1;

var env, snd;
env = Env.linen(0.5, decay, 0).kr(doneAction: 2);
snd = Pluck.ar(
		in: WhiteNoise.ar(amp),
		trig: Impulse.kr(0),
		maxdelaytime: 0.1,
		delaytime: (freq*2).reciprocal,
		decaytime: decay,
		coef: dampen);
	snd = snd * env;
	Out.ar(0, [snd, snd]);
}).add;

SynthDef("sawSynth", { arg freq = 440, amp = 0.1, att = 0.1, rel = 2, lofreq = 1000, hifreq = 3000;
	var env, snd;
	env = Env.perc(
		attackTime: att,
		releaseTime: rel,
		level: amp
	).kr(doneAction: 2);
	snd = Saw.ar(freq: freq * [0.99, 1, 1.001, 1.008], mul: env);
	snd = LPF.ar(
		in: snd,
		freq: LFNoise2.kr(1).range(lofreq, hifreq)
	);
	snd = Splay.ar(snd);
	Out.ar(0, snd);
}).add;
)


///////////////////////////////////////
//////// RUN ME vvvvv RUN ME //////////
///////////////////////////////////////
(
// 113/60 is the BPM of the song propper.
t = TempoClock.new(128/60);
// 'transp1' should be '1' in the song propper.
~transp1 = 0;
~transp2 = ~transp1 - 1;
{
var chords = Pbind(
	\instrument, \sawSynth,
	\midinote, Pseq([
		[35, 38, 42],
		[31, 35, 38],
		[28, 31, 35],
		[35, 38, 43]
	] + ~transp1, inf) + 12,
	\dur, 8,
	\amp, 0.6,
	\rel, 8
);

var hibells = Pbind(
	\instrument, \plucking,
	\midinote, Pseq([
		67, 66, 64, 62, 59, 62, 64, 66
	] + ~transp1, inf) + 12,
	\dur, 1,
	\amp, 1,
	\rel, 1
);

var hibells2 = Pbind(
	\instrument, \sawSynth,
	\midinote, Pseq([
		67, 66, 64, 62, 59, 62, 64, 66
	] + ~transp1, inf) + 12,
	\dur, 1,
	\amp, 0.2,
	\rel, 1
);

var kick1 = Pbind(
	\instrument, "kick",
	\midinote, Pseq([1], inf),
	\amp, Pseq([2, 2, 0, 0], inf),
	\rel, 1,
	\dur, Pseq([1.5, 0.5, 1, 1], inf),
	\sinfreq, Pseq([45, 45, \rest, \rest], inf),
	\glissf, 0.5
);

var hihats = Pbind(
	\instrument, "hihat",
	\dur, 0.5,
	\amp, Pseq([0.1, 0.3, 0.3, 0.3], inf),
	\glissf, 0.2,
	\rel, 0.05
);

var clap = Pbind(
	\instrument, "hihat",
	\midinote, Pseq([1], inf),
	\dur, 1,
	\amp, Pseq([0, 0, 1, 0], inf),
	\ffreq, 2000,
	\glissf, 0.2,
	\rel, 1.5
);

var scream = Pbind(
	\instrument, "kick",
	\sinfreq, Pseq([500], 1),
	\dur, 10,
	\amp, 0.6,
	\rel, 4,
	\glissf, 10
);

var abel0 = Pbind(
	\instrument, \sawSynth,
	\midinote, Pseq([
		66, 66, 66, 66, 69, 66, 64, 62, 64, 62
	] + ~transp1, 4),
	\dur, Pseq([
		0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 1, 3
	], 4),
	\amp, 0.9,
	\rel, 1
);
var abel1 = Pbind(
	\instrument, \sawSynth,
	\midinote, Pseq([
		66, 66, 66, 69, 66, 64, 62,
	] + ~transp1, 1) + 12,
	\dur, 0.5,
	\amp, 0.9,
	\rel, 1
);
var abel2 = Pbind(
	\instrument, \sawSynth,
	\midinote, Pseq([
		64,59,62
	] + ~transp1, 1) + 12,
	\dur, 2,
	\amp, 0.9,
	\rel, 1
);
var abel3 = Pbind(
	\instrument, \sawSynth,
	\midinote, Pseq([
		64, 64, 66, 64, 62
	] + ~transp1, 1) + 12,
	\dur, Pseq([2, 0.2, 0.2, 1.6, 2], 1),
	\amp, 0.9,
	\rel, 1
);
var abel4 = Pbind(
	\instrument, \sawSynth,
	\midinote, Pseq([
		64, 59, 62, 64, 59, 62,
	] + ~transp1, 3) + 12,
	\dur,  Pseq([0.75,0.75,0.5,
			0.75,0.75,0.5], 3),
	\amp, 0.9,
	\rel, 1
);

var piano = Pbind(
	\instrument, \plucking,
	\midinote, Pseq([
			Pn([60, 63, 67], 8),
			Pn([68, 72, 63], 8),
			Pn([65, 68, 72], 8),
			Pn([68, 71, 63], 8),
	] + ~transp2, 2),
	\dur, 1,
	\amp, 0.6,
	\rel, 1
);

var piano2 = Pbind(
	\instrument, \sawSynth,
	\midinote, Pseq([
			Pn([60, 63, 67], 8),
			Pn([68, 72, 63], 8),
			Pn([65, 68, 72], 8),
			Pn([68, 71, 63], 8),
	] + ~transp2, 2),
	\dur, 1,
	\amp, 0.3,
	\rel, 1
);

var abel6 = Pbind(
	\instrument, \sawSynth,
	\midinote, Pseq([
		\rest, \rest,
		72, 82, 82, 79,
		72, 82, 82, 74,
		72, 70, 72
	] + ~transp2, 1),
	\dur, Pseq([
		1, 0.5,
		0.5, 0.75, 0.75, 2,
		0.5, 0.75, 0.75, 3,
		0.5, 0.5, 2
	], 4),
	\amp, 0.9,
	\rel, 1
);


var abel7 = Pbind(
	\instrument, \sawSynth,
	\midinote, Pseq([
		79, 82, 84,
		79, 77, 75,

		\rest,

		79, 82, 84,
		79, 77, 75,
		75, 74, 70, 72
	] + ~transp2, 1),
	\dur, Pseq([
		0.25, 0.25, 3.5,
		0.5, 0.5, 2.5,

		0.5,

		0.25, 0.25, 3.5,
		0.5, 0.5, 2.5,
		0.5, 0.5, 0.5, 0.5
	], 4),
	\amp, 0.9,
	\rel, 1
);

var abel8 = Pbind(
	\instrument, \sawSynth,
	\midinote, Pseq([
		79, 82, 84,
		79, 77, 75,

		\rest,

		79, 82, 84,
		84, 87, 86
	] + ~transp2, 1),
	\dur, Pseq([
		0.25, 0.25, 3.5,
		0.5, 0.5, 2.5,

		0.5,

		0.25, 0.25, 3.5,
		0.5, 0.5, 3.5
	], 4),
	\amp, 0.9,
	\rel, 1
);

	~chordsPlayer = chords.play(t);
	32.wait;
	abel0.play(t); //Your man on the road...
	~hibellsPlayer = hibells.play(t);
	~hibells2Player = hibells2.play(t);
	32.wait;
	~hihatsPlayer = hihats.play(t);
	~clapPlayer = clap.play(t);
	abel0.play(t); //I can't find your house...
	30.wait;
	~chordsPlayer.stop;
	~hibellsPlayer.stop;
	~hibells2Player.stop;
	~hihatsPlayer.stop;
	~clapPlayer.stop;

	scream.play(t); //AHHHHHH
	2.25.wait;

	abel1.play(t); //I only call you when it's
	3.5.wait;
	~chordsPlayer.reset.play(t);
	~hihatsPlayer.reset.play(t);
	~clapPlayer.reset.play(t);
	~kick1Player = kick1.play(t);
	abel2.play(t); //Half-past-five.
	4.5.wait;
	abel1.play(t); //The only time that I'll
	3.5.wait;
	abel2.play(t); //be by your side
	4.5.wait;
	abel1.play(t); //I only love it when you
	3.5.wait;
	abel4.play(t); //Touch me, Not feel me... uh... awkardddddd.... etc.
	12.5.wait;

	abel1.play(t); //I only call you when it's
	3.5.wait;
	abel2.play(t); //Half-past-five.
	4.5.wait;
	abel1.play(t); //The only time that I'll
	3.5.wait;
	abel3.play(t); //be by yOUr side
	4.5.wait;
	abel1.play(t); //I only love it when you
	3.5.wait;
	abel4.play(t); //Touch me, Not feel me... uh... awkardddddd.... etc.
	12.5.wait;
	~chordsPlayer.stop;
	~hihatsPlayer.stop;
	2.wait;
	~kick1Player.stop;
	~clapPlayer.stop;
	1.5.wait;

	///////////////////////////////////// Verse + Chorus

	abel0.play(t);
	~chordsPlayer.reset.play(t);
	~hibellsPlayer = hibells.play(t);
	~hibells2Player = hibells2.play(t);
	32.wait;
	~hihatsPlayer = hihats.play(t);
	~clapPlayer = clap.play(t);
	abel0.play(t);
	30.wait;
	~chordsPlayer.stop;
	~hibellsPlayer.stop;
	~hibells2Player.stop;
	~hihatsPlayer.stop;
	~clapPlayer.stop;

	scream.play(t); //AHHHHHH
	2.25.wait;

	abel1.play(t); //I only call you when it's
	3.5.wait;
	~chordsPlayer.reset.play(t);
	~hihatsPlayer.reset.play(t);
	~clapPlayer.reset.play(t);
	~kick1Player = kick1.play(t);
	abel2.play(t); //Half-past-five.
	4.5.wait;
	abel1.play(t); //The only time that I'll
	3.5.wait;
	abel3.play(t); //be by your side
	4.5.wait;
	abel1.play(t); //I only love it when you
	3.5.wait;
	abel4.play(t); //Touch me, Not feel me... uh... awkardddddd.... etc.
	12.5.wait;

	abel1.play(t); //I only call you when it's
	3.5.wait;
	abel2.play(t); //Half-past-five.
	4.5.wait;
	abel1.play(t); //The only time that I'll
	3.5.wait;
	abel3.play(t); //be by your side
	4.5.wait;
	abel1.play(t); //I only love it when you
	3.5.wait;
	abel4.play(t); //Touch me, Not feel me... uh... awkardddddd.... etc.
	12.5.wait;
	~chordsPlayer.stop;
	~hihatsPlayer.stop;
	2.wait;
	~kick1Player.stop;
	~clapPlayer.stop;
	1.5.wait;

	/////////////////////////////////////  Bridge

	~pianoPlayer = piano.play(t);
	~piano2Player = piano2.play(t);
	abel6.play(t); //The hills have eyes...
	15.wait;
	abel7.play(t); //Who are you... Who are you...
	17.wait;
	abel6.play(t); //Hide your eyes...
	15.wait;
	abel8.play(t); //Who are you... Who are youuuuu...
	19.wait;

	///////////////////////////////////// Chorus Only

	abel1.play(t); //I only call you when it's
	3.5.wait;
	~chordsPlayer.reset.play(t);
	~hihatsPlayer.reset.play(t);
	~clapPlayer.reset.play(t);
	~kick1Player = kick1.play(t);
	abel2.play(t); //Half-past-five.
	4.5.wait;
	abel1.play(t); //The only time that I'll
	3.5.wait;
	abel3.play(t); //be by your side
	4.5.wait;
	abel1.play(t); //I only love it when you
	3.5.wait;
	abel4.play(t); //Touch me, Not feel me... uh... awkardddddd.... etc.
	12.5.wait;

	abel1.play(t); //I only call you when it's
	3.5.wait;
	abel2.play(t); //Half-past-five.
	4.5.wait;
	abel1.play(t); //The only time that I'll
	3.5.wait;
	abel3.play(t); //be by your side
	4.5.wait;
	abel1.play(t); //I only love it when you
	3.5.wait;
	abel4.play(t); //Touch me, Not feel me... uh... awkardddddd.... etc.
	12.5.wait;
	~chordsPlayer.stop;
	~hihatsPlayer.stop;
	2.wait;
	~kick1Player.stop;
	~clapPlayer.stop;
	1.5.wait;

}.fork(t);
)

////////////////////////
////////////////////////


//// Música 2 - Usou o Granular ////



////////////////////////
////////////////////////


//// Música 3 /////


(
////////////////////////////////////////////////////////////////
// EPIC SAX GUY SynthDefs
// http://www.youtube.com/watch?v=KHy7DGLTt8g
// Not yet there... but hearable

// sounds more like a trumpet/horn after the failure and cheesyness of the stk sax
SynthDef(\sax, { |out, freq=440, amp=0.1, gate=1|
	var num = 30;
	var harms = Array.series(num, 1, 1) * Array.exprand(num, 0.995, 1.001);
	var snd = SinOsc.ar(freq * SinOsc.kr(Rand(2.0,5.0),0,Rand(0.001, 0.01),1) * harms, mul:Array.geom(num, 1, 0.63));
	snd = Splay.ar(snd);
	snd = BBandPass.ar(snd, freq * XLine.kr(0.1,4,0.01), 2);
	snd = snd * amp * EnvGen.ar(Env.adsr(0.001, 0.2, 0.7, 0.2), gate, doneAction:2);
	Out.ar(out, snd!2);
}).add;

// should be more like a gated synth, but this one gives the rhythmic element
// remember to pass the bps from the language tempo!
SynthDef(\lead, { |out, freq=440, amp=0.1, gate=1, bps=2|
    var snd;
    var seq = Demand.kr(Impulse.kr(bps*4), 0, Dseq(freq*[1,3,2], inf)).lag(0.01);
    snd = LFSaw.ar(freq*{rrand(0.995, 1.005)}!4);
    snd = Splay.ar(snd);
    snd = MoogFF.ar(snd, seq, 0.5);
    snd = snd * EnvGen.ar(Env.asr(0.01,1,0.01), gate, doneAction:2);
    OffsetOut.ar(out, snd * amp);
}).add;

// yep, an organ with a sub bass tone :D
SynthDef(\organ, { |out, freq=440, amp=0.1, gate=1|
    var snd;
    snd = Splay.ar(SinOsc.ar(freq*Array.geom(4,1,2), mul:1/4));
    snd = snd + SinOsc.ar(freq/2, mul:0.4)!2;
    snd = snd * EnvGen.ar(Env.asr(0.001,1,0.01), gate, doneAction:2);
    OffsetOut.ar(out, snd * amp);
}).add;

// from the synth def pool
SynthDef(\kick, { |out=0, amp=0.1, pan=0|
	var env0, env1, env1m, son;

	env0 =  EnvGen.ar(Env.new([0.5, 1, 0.5, 0], [0.005, 0.06, 0.26], [-4, -2, -4]), doneAction:2);
	env1 = EnvGen.ar(Env.new([110, 59, 29], [0.005, 0.29], [-4, -5]));
	env1m = env1.midicps;

	son = LFPulse.ar(env1m, 0, 0.5, 1, -0.5);
	son = son + WhiteNoise.ar(1);
	son = LPF.ar(son, env1m*1.5, env0);
	son = son + SinOsc.ar(env1m, 0.5, env0);

	son = son * 1.2;
	son = son.clip2(1);

	OffsetOut.ar(out, Pan2.ar(son * amp));
}).add;

// full of fail:

//SynthDef(\sax, { |out, freq=440, amp=0.1, gate=1|
//	var r_stiff = 67;
//	var r_ap = 63;
//	var noise = 10;
//	var pos = 20;
//	var vibf = 20;
//	var vibg = 1;
//	var press = 85;
//	var snd = StkSaxofony.ar(freq, r_stiff, r_ap, noise, pos, vibf, vibg, press, 1, amp);
//	snd = snd * EnvGen.ar(Env.adsr(0.001, 0.2, 0.7, 0.2), gate, doneAction:2);
//	Out.ar(out, snd!2);
//}).add;


)


////////////////////////////////////////////////////////////////
// EPIC SAX GUY TUNE
// http://www.youtube.com/watch?v=KHy7DGLTt8g
// ... still needs a nice gated pad

(
TempoClock.default.tempo = 2.1;

Pdef(\kick).quant = 8;
Pdef(\organ).quant = 4;
Pdef(\sax).quant = 4;
Pdef(\lead).quant = 4;

////////////////////////////////////////////////////////////////
Pdef(\kick, Pbind(\instrument, \kick, \dur, 1, \amp, 1)).play;

Pdef(\organ, Pbind(
	\instrument, \organ,
	\octave, [3,4],
	\root, 3,
	\scale, Scale.minor,
	\degree, Pstutter(3, Pseq([0,-2,2,4], inf)),
	\amp, 0.3,
	\dur, Pseq([1.5,1.5,1], inf)
)).play;

Pdef(\lead, Pbind(
	\instrument, \lead,
	\octave, [5,6],
	\root, 3,
	\scale, Scale.minor,
	\degree, Pseq([0,2,0,4], inf),
	\amp, 0.2,
	\bps, TempoClock.default.tempo,
	\dur, 4
)).play;

// needs more articulation...
Pdef(\sax, Pbind(
	\instrument, \sax,
	\root, 3,
	\scale, Scale.minor,
	\octave, 5,
	\legato, 0.75,
	\amp, Pwhite(0.9,1.0, inf),
	\degree, Pseq([Pseq([Pn(4,4),3,4],2), Pseq([4,6,4,3,2,0,0,1,2,0])], inf),
	\dur, Pseq([Pseq([2,1/2,Pn(1/4,3),3/4],2), Pseq([1.5,1,1,1,1,Pn(0.5,5)])], inf)
)).play;
)