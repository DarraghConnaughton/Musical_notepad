!function(a){if("object"==typeof exports&&"undefined"!=typeof module)module.exports=a();else if("function"==typeof define&&define.amd)define([],a);else{var b;b="undefined"!=typeof window?window:"undefined"!=typeof global?global:"undefined"!=typeof self?self:this,b.musicaljs=a()}}(function(){return function a(b,c,d){function e(g,h){if(!c[g]){if(!b[g]){var i="function"==typeof require&&require;if(!h&&i)return i(g,!0);if(f)return f(g,!0);var j=new Error("Cannot find module '"+g+"'");throw j.code="MODULE_NOT_FOUND",j}var k=c[g]={exports:{}};b[g][0].call(k.exports,function(a){var c=b[g][1][a];return e(c?c:a)},k,k.exports,a,b,c,d)}return c[g].exports}for(var f="function"==typeof require&&require,g=0;g<d.length;g++)e(d[g]);return e}({1:[function(a,b,c){var d=a("./instrument"),e=a("./parser-abc");window.Instrument=d,window.parseABCFile=e,b.exports={Instrument:d,parseABCFile:e}},{"./instrument":2,"./parser-abc":3}],2:[function(a,b,c){function d(a){this._atop=f(),this._timbre=g(a,this._atop),this._queue=[],this._minQueueTime=1/0,this._maxScheduledTime=0,this._unsortedQueue=!1,this._startSet=[],this._finishSet={},this._cleanupSet=[],this._callbackSet=[],this._handlers={},this._now=null,h()&&this.silence()}var e=a("./utils"),f=e.getAudioTop,g=e.makeTimbre,h=e.isAudioPresent,i=e.defaultTimbre,j=e.pitchToMidi,k=e.midiToFrequency,l=e.audioCurrentStartTime,m=e.makeOscillator,n=e.midiToPitch,o=a("./parser-abc");d.timeOffset=.0625,d.dequeueTime=.5,d.bufferSecs=2,d.toneLength=1,d.cleanupDelay=.1,d.prototype.setTimbre=function(a){this._timbre=g(a,this._atop)},d.prototype.getTimbre=function(a){return g(this._timbre,this._atop)},d.prototype.setVolume=function(a){this._out&&(isNaN(a)||(this._out.gain.value=a))},d.prototype.getVolume=function(a){return this._out?this._out.gain.value:0},d.prototype.silence=function(){var a,b,c,d=1;this._queue.length=0,this._minQueueTime=1/0,this._maxScheduledTime=0,this._startSet.length=0,b=this._finishSet,this._finishSet={},c=this._callbackSet,this._callbackSet=[],this._out&&(this._out.disconnect(),d=this._out.gain.value),this._atop=f(),this._out=this._atop.ac.createGain(),this._out.gain.value=d,this._out.connect(this._atop.out);for(a in b)this._trigger("noteoff",b[a]);for(a=0;a<c.length;++a)c[a].callback()},d.prototype.now=function(){return null!=this._now?this._now:(this._startPollTimer(!0),this._now)},d.prototype.on=function(a,b){this._handlers.hasOwnProperty(a)||(this._handlers[a]=[]),this._handlers[a].push(b)},d.prototype.off=function(a,b){if(this._handlers.hasOwnProperty(a))if(b){var c,d=this._handlers[a];for(c=0;c<d.length;++c)d[c]===b&&(d.splice(c,1),c-=1)}else this._handlers[a]=[]},d.prototype._trigger=function(a,b){var c,d=this._handlers[a];if(d){if(1==d.length)return void d[0](b);for(d=d.slice(),c=0;c<d.length;++c)d[c](b)}},d.prototype._makeSound=function(a){var b,c,e,f,g=a.timbre||this._timbre,h=a.time+d.timeOffset,i=h+a.duration,j=Math.min(i,h+g.attack),k=g.decay*Math.pow(440/a.frequency,g.decayfollow),l=j,n=i+g.release,o=g.detune&&1!=g.detune,p=g.gain*a.velocity*(o?.5:1),q=this._atop.ac;if(a.duration>0&&a.velocity>0){for(b=q.createGain(),b.gain.setValueAtTime(0,h),b.gain.linearRampToValueAtTime(p,j);j+1/32>l&&i>l+1/256;)l+=1/256,b.gain.linearRampToValueAtTime(p*(g.sustain+(1-g.sustain)*Math.exp((j-l)/k)),l);b.gain.setTargetAtTime(p*g.sustain,l,k),b.gain.setValueAtTime(p*(g.sustain+(1-g.sustain)*Math.exp((j-i)/k)),i),b.gain.linearRampToValueAtTime(0,n),b.connect(this._out),!g.cutoff&&!g.cutfollow||g.cutoff==1/0?c=b:(c=q.createBiquadFilter(),c.frequency.value=g.cutoff+a.frequency*g.cutfollow,c.Q.value=g.resonance,c.connect(b)),e=m(this._atop,g.wave,a.frequency),e.connect(c),e.start(h),e.stop(n),o&&(f=m(this._atop,g.wave,a.frequency*g.detune),f.connect(c),f.start(h),f.stop(n)),a.gainNode=b,a.oscillators=[e],o&&a.oscillators.push(f),a.cleanuptime=n}else a.duration=0;this._startSet.push(a)},d.prototype._truncateSound=function(a,b){if(b<a.time+a.duration&&(a.duration=Math.max(0,b-a.time),a.gainNode)){var c,e=a.timbre||this._timbre,f=a.time+d.timeOffset,g=b+d.timeOffset,h=Math.min(g,f+e.attack),i=e.decay*Math.pow(440/a.frequency,e.decayfollow),j=g+e.release,k=j+d.cleanupDelay,l=e.detune&&1!=e.detune,m=e.gain*a.velocity*(l?.5:1),n=a.gainNode;if(n.gain.cancelScheduledValues(g),f>=g?n.gain.setValueAtTime(0,g):h>=g?n.gain.linearRampToValueAtTime(m*(g-f)/(h-f)):n.gain.setValueAtTime(m*(e.sustain+(1-e.sustain)*Math.exp((h-g)/i)),g),n.gain.linearRampToValueAtTime(0,j),a.oscillators)for(c=0;c<a.oscillators.length;++c)a.oscillators[c].stop(j);a.cleanuptime=k}},d.prototype._doPoll=function(){if(this._pollTimer=null,this._now=null,window.interrupted)return void this.silence();var a,b,c,e,f,g,h,i,j=this._atop.ac.currentTime+5e-4,k=[];if(this._minQueueTime-j<=d.bufferSecs){for(this._unsortedQueue&&(this._queue.sort(function(a,b){return a.time!=b.time?a.time-b.time:a.duration!=b.duration?a.duration-b.duration:a.frequency-b.frequency}),this._unsortedQueue=!1),a=0;a<this._queue.length&&!(this._queue[a].time-j>d.bufferSecs);++a);if(a>0){for(b=this._queue.splice(0,a),a=0;a<b.length;++a)this._makeSound(b[a]);this._minQueueTime=this._queue.length>0?this._queue[0].time:1/0}}for(a=0;a<this._cleanupSet.length;++a)f=this._cleanupSet[a],f.cleanuptime<j&&(f.gainNode&&(f.gainNode.disconnect(),f.gainNode=null),this._cleanupSet.splice(a,1),a-=1);for(e in this._finishSet)f=this._finishSet[e],c=f.time+f.duration,j>=c&&(k.push({order:[c,0],f:this._trigger,t:this,a:["noteoff",f]}),f.cleanuptime!=1/0&&this._cleanupSet.push(f),delete this._finishSet[e]);for(a=0;a<this._callbackSet.length;++a)i=this._callbackSet[a],c=i.time,j>=c&&(k.push({order:[c,1],f:i.callback,t:null,a:[]}),this._callbackSet.splice(a,1),a-=1);for(a=0;a<this._startSet.length;++a)this._startSet[a].time<=j&&(h=f=this._startSet[a],e=f.frequency,g=null,this._finishSet.hasOwnProperty(e)&&(g=this._finishSet[e],g.time<f.time||g.time==f.time&&g.duration<f.duration?(this._truncateSound(g,f.time),k.push({order:[f.time,0],f:this._trigger,t:this,a:["noteoff",g]}),delete this._finishSet[e]):(this._truncateSound(f,g.time),g=f)),this._startSet.splice(a,1),a-=1,f.duration>0&&f.velocity>0&&g!==f&&(this._finishSet[e]=f,k.push({order:[f.time,2],f:this._trigger,t:this,a:["noteon",f]})));for(this._startPollTimer(),k.sort(function(a,b){return a.order[0]!=b.order[0]?a.order[0]-b.order[0]:a.order[1]-b.order[1]}),a=0;a<k.length;++a)i=k[a],i.f.apply(i.t,i.a)},d.prototype._startPollTimer=function(a){if(!this._pollTimer||null==this._now){var b,c,e=this,f=function(){e._doPoll()},g=1/0;if(this._pollTimer&&(clearTimeout(this._pollTimer),this._pollTimer=null),a)return this._now=l(),void(this._pollTimer=setTimeout(f,0));for(b=0;b<this._startSet.length;++b)g=Math.min(g,this._startSet[b].time);for(b in this._finishSet)g=Math.min(g,this._finishSet[b].time+this._finishSet[b].duration);for(b=0;b<this._callbackSet.length;++b)g=Math.min(g,this._callbackSet[b].time);this._cleanupSet.length>0&&(g=Math.min(g,this._cleanupSet[0].cleanuptime+1)),g=Math.min(g,this._minQueueTime-d.dequeueTime),c=Math.max(.001,g-this._atop.ac.currentTime),isNaN(c)||c==1/0||(this._pollTimer=setTimeout(f,Math.round(1e3*c)))}},d.prototype.tone=function(a,b,c,e,f,g){if(this._atop){"object"==typeof a&&(null==c&&(c=a.velocity),null==b&&(b=a.duration),null==e&&(e=a.delay),null==f&&(f=a.timbre),null==g&&(g=a.origin),a=a.pitch);var h,l;if(a||(a="C"),isNaN(a)?(h=j(a),l=k(h)):(l=Number(a),0>l?(h=-l,l=k(h)):h=frequencyToMidi(l)),f||(f=this._timbre),f!==this._timbre){var m,n=f;f={};for(m in i)m in n?f[m]=n[m]:f[m]=defaulTimbre[m]}var o=(this._atop.ac,this.now()),p=o+(e||0),q={time:p,on:!1,frequency:l,midi:h,velocity:null==c?1:c,duration:null==b?d.toneLength:b,timbre:f,instrument:this,gainNode:null,oscillators:null,cleanuptime:1/0,origin:g};p<o+d.bufferSecs?this._makeSound(q):(!this._unsortedQueue&&this._queue.length&&p<this._queue[this._queue.length-1].time&&(this._unsortedQueue=!0),this._queue.push(q),this._minQueueTime=Math.min(this._minQueueTime,q.time))}},d.prototype.schedule=function(a,b){this._callbackSet.push({time:this.now()+a,callback:b})},d.prototype.play=function(a){var b,c,d,e,f,h,i,j,k,l,m,n,p,q,r,s,t=Array.prototype.slice.call(arguments),u=null,v={},w=0,x=[];if(t.length&&"function"==typeof t[t.length-1]&&(u=t.pop()),!this._atop)return void(u&&u());if(d=0,"object"==typeof t[0]){for(f in t[0])t[0].hasOwnProperty(f)&&(v[f]=t[0][f]);d=1,v.song&&t.push(v.song)}for(;d<t.length;++d)for(b=t[d].split(/\n(?=X:)/),f=0;f<b.length;++f)c=o(b[f]),c&&(!v.tempo&&c.tempo&&(v.tempo=c.tempo,c.unitbeat&&(v.tempo*=c.unitbeat/(c.unitnote||1))),c.voice&&x.push(c));for(v.tempo||(v.tempo=120),null==v.volume&&(v.volume=1),q=60/v.tempo,f=0;f<x.length;++f){c=x[f];for(l in c.voice)if(e=g(v.timbre||c.voice[l].timbre||c.timbre||this._timbre,this._atop),j=c.voice[l].stems){for(h=0,k=0;k<j.length;++k){for(n=j[k],i=1/Math.sqrt(n.notes.length),m=0;m<n.notes.length;++m)p=n.notes[m],p.holdover||(r=(p.time||n.time)*q,n.staccato?r=Math.min(Math.min(r,q/16),e.attack+e.decay):!p.slurred&&r>=1/8&&(r-=1/32),s=(p.velocity||1)*i*v.volume,this.tone(p.pitch,r,s,h,e,p));h+=n.time*q}w=Math.max(h,w)}}this._maxScheduledTime=Math.max(this._maxScheduledTime,this.now()+w),u&&this.schedule(w,u)},d.pitchToMidi=function(a){return"string"==typeof a?j(a):a},d.midiToPitch=function(a){return"number"==typeof a?n(a):a},b.exports=d},{"./parser-abc":3,"./utils":4}],3:[function(a,b,c){var d=a("./utils"),e=d.pitchToFrequency,f=/^([A-Za-z]):\s*(.*)$/,g=/(?:\[[A-Za-z]:[^\]]*\])|\s+|%[^\n]*|![^\s!:|\[\]]*!|\+[^+|!]*\+|[_<>@^]?"[^"]*"|\[|\]|>+|<+|(?:(?:\^+|_+|=|)[A-Ga-g](?:,+|'+|))|\(\d+(?::\d+){0,2}|\d*\/\d+|\d+\/?|\/+|[xzXZ]|\[?\|\]?|:?\|:?|::|./g;b.exports=function(a){function b(a,b){switch(a){case"V":D!==C&&c(b.split(" ")[0]);break;case"M":i(b,D);break;case"L":j(b,D);break;case"Q":k(b,D)}D.hasOwnProperty(a)?D[a]+="\n"+b:D[a]=b,"K"==a&&(E=n(b),D===C&&c(d()))}function c(a){a=a||"",(a||D===C)&&(C.voice||(C.voice={}),C.voice.hasOwnProperty(a)?(D=C.voice[a],F=D.accent):(D={id:a,accent:{slurred:0}},C.voice[a]=D,F=D.accent))}function d(){return C.V?C.V.split(/\s+/)[0]:""}function h(a){var e,f=a.match(g),h=null,i=0,j=0,k=null;if(!f)return null;for(;i<f.length;)if(/^[\s%]/.test(f[i]))i++;else if(/^\[[A-Za-z]:[^\]]*\]$/.test(f[i]))b(f[i].substring(1,2),f[i].substring(3,f[i].length-1).trim()),i++;else if(/</.test(f[i]))j=-f[i++].length;else if(/>/.test(f[i]))j=f[i++].length;else if(/^\(\d+(?::\d+)*/.test(f[i]))k=r(f[i++]);else if(/^[!+].*[!+]$/.test(f[i]))s(f[i++],F);else if(/^.?".*"$/.test(f[i]))i++;else if(/^[()]$/.test(f[i]))"("==f[i++]?F.slurred+=1:(F.slurred-=1,F.slurred<=0&&(F.slurred=0,D.stems&&D.stems.length>=1&&p(D.stems[D.stems.length-1],!1)));else if(/\|/.test(f[i])){for(e in F)1==e.length&&delete F[e];i++}else h=t(f,i,E,F),null!==h?(k&&(q(h.stem,k.time),k.count-=1,k.count||(k=null)),j&&D.stems&&D.stems.length&&(e=j>0?(1-Math.pow(.5,j))*h.stem.time:(Math.pow(.5,-j)-1)*D.stems[D.stems.length-1].time,o(D.stems[D.stems.length-1],e),o(h.stem,-e)),j=0,F.slurred&&p(h.stem,!0),D===C&&c(d()),"stems"in D||(D.stems=[]),D.stems.push(h.stem),i=h.index):i++}function i(a,b){var c=/^C/.test(a)?1:w(a);c&&(b.unitnote||(.75>c?b.unitnote=1/16:b.unitnote=1/8))}function j(a,b){var c=w(a);c&&(b.unitnote=c)}function k(a,b){var c,d=a.split(/\s+|=/),e=null,f=null;for(c=0;c<d.length;++c)d[c].indexOf("/")>=0||/^[1-4]$/.test(d[c])?e=e||w(d[c]):f=f||Number(d[c]);e&&(b.unitbeat=e),f&&(b.tempo=f)}function l(a){var b,c,d,e,f,g={};for(c=0;c<a.length;++c){for(b={},d=0;d<a[c].notes.length;++d)f=e=a[c].notes[d],g.hasOwnProperty(e.pitch)&&(f=g[e.pitch],f.time+=e.time,e.holdover=!0),e.tie&&(b[e.pitch]=f);g=b}}function m(a){var b,c="FCGDAEB",d={};if(!a)return d;if(a>0)for(b=0;a>b&&7>b;++b)d[c.charAt(b)]="^";else for(b=0;b>a&&b>-7;--b)d[c.charAt(6+b)]="_";return d}function n(a){if(!a)return{};var b,c={"c#":7,"f#":6,b:5,e:4,a:3,d:2,g:1,c:0,f:-1,bb:-2,eb:-3,ab:-4,db:-5,gb:-6,cb:-7,"a#m":7,"d#m":6,"g#m":5,"c#m":4,"f#m":3,bm:2,em:1,am:0,dm:-1,gm:-2,cm:-3,fm:-4,bbm:-5,ebm:-6,abm:-7,"g#mix":7,"c#mix":6,"f#mix":5,bmix:4,emix:3,amix:2,dmix:1,gmix:0,cmix:-1,fmix:-2,bbmix:-3,ebmix:-4,abmix:-5,dbmix:-6,gbmix:-7,"d#dor":7,"g#dor":6,"c#dor":5,"f#dor":4,bdor:3,edor:2,ador:1,ddor:0,gdor:-1,cdor:-2,fdor:-3,bbdor:-4,ebdor:-5,abdor:-6,dbdor:-7,"e#phr":7,"a#phr":6,"d#phr":5,"g#phr":4,"c#phr":3,"f#phr":2,bphr:1,ephr:0,aphr:-1,dphr:-2,gphr:-3,cphr:-4,fphr:-5,bbphr:-6,ebphr:-7,"f#lyd":7,blyd:6,elyd:5,alyd:4,dlyd:3,glyd:2,clyd:1,flyd:0,bblyd:-1,eblyd:-2,ablyd:-3,dblyd:-4,gblyd:-5,cblyd:-6,fblyd:-7,"b#loc":7,"e#loc":6,"a#loc":5,"d#loc":4,"g#loc":3,"c#loc":2,"f#loc":1,bloc:0,eloc:-1,aloc:-2,dloc:-3,gloc:-4,cloc:-5,floc:-6,bbloc:-7},d=a.replace(/\s+/g,"").toLowerCase().substr(0,5),e=d.match(/maj|min|mix|dor|phr|lyd|loc|m/);b=e?"maj"==e?d.substr(0,e.index):"min"==e?d.substr(0,e.index+1):d.substr(0,e.index+e[0].length):/^[a-g][#b]?/.exec(d)||"";var f=m(c[b]),g=a.substr(b.length).match(/(_+|=|\^+)[a-g]/gi);if(g)for(var h=0;h<g.length;++h){var i=g[h].charAt(g[h].length-1).toUpperCase();"="==g[h].charAt(0)?delete f[i]:f[i]=g[h].substr(0,g[h].length-1)}return f}function o(a,b){var c,d,e=a.time,f=e+b;for(a.time=f,c=0;c<a.notes.length;++c)d=a.notes[c],d.time==e&&(d.time=f)}function p(a,b){var c,d;for(c=0;c<a.notes.length;++c)d=a.notes[c],b?d.slurred=!0:d.slurred&&delete d.slurred}function q(a,b){var c;for(a.time*=b,c=0;c<a.notes.length;++c)a.notes[c].time*=b}function r(a){var b=/^\((\d+)(?::(\d+)(?::(\d+))?)?$/.exec(a);if(!b)return null;var c=Number(b[1]),d=Number(b[2])||2,e=Number(b[3])||c;return{time:d/c,count:e}}function s(a,b){if(!(a.length<2))switch(a=a.substring(1,a.length-1)){case"pppp":case"ppp":b.dynamics=.2;break;case"pp":b.dynamics=.4;break;case"p":b.dynamics=.6;break;case"mp":b.dynamics=.8;break;case"mf":b.dynamics=1;break;case"f":b.dynamics=1.2;break;case"ff":b.dynamics=1.4;break;case"fff":case"ffff":b.dynamics=1.5}}function t(a,b,c,d){var f,g,h,i,j=[],k="",l=!1,m=null,n=1/0;if(b<a.length&&"."==a[b]&&(l=!0,b++),b<a.length&&"["==a[b]){for(b++;b<a.length;)if(/^[\s%]/.test(a[b]))b++;else{if(/[A-Ga-g]/.test(a[b]))m={pitch:v(a[b++],c,d),tie:!1},m.frequency=e(m.pitch),j.push(m);else{if(!/[xzXZ]/.test(a[b])){if("."==a[b]){l=!0,b++;continue}break}m=null,b++}b<a.length&&/^(?![\s%!]).*[\d\/]/.test(a[b])?(f=a[b++],g=w(f)):(f="",g=1),m&&(m.duration=f,m.time=g),g&&n>g&&(k=f,n=g),b<a.length&&"-"==a[b]&&(m&&(j[j.length-1].tie=!0),b++)}if("]"!=a[b])return null;b++}else if(b<a.length&&/[A-Ga-g]/.test(a[b]))m={pitch:v(a[b++],c,d),tie:!1,duration:"",time:1},m.frequency=e(m.pitch),j.push(m);else{if(!(b<a.length&&/^[xzXZ]$/.test(a[b])))return null;b++}if(b<a.length&&/^(?![\s%!]).*[\d\/]/.test(a[b]))for(k=a[b++],g=w(k),i=0;i<j.length;++i)j[i].duration=k,j[i].time=g;if(b<a.length&&"-"==a[b])for(b++,i=0;i<j.length;++i)j[i].tie=!0;if(d.dynamics)for(h=d.dynamics,i=0;i<j.length;++i)j[i].velocity=h;return{index:b,stem:{notes:j,duration:k,staccato:l,time:w(k)}}}function u(a){return a.length>0&&"="==a.charAt(0)?a.substr(1):a}function v(a,b,c){var d,e=/^(\^+|_+|=|)([A-Ga-g])(.*)$/.exec(a);return e?(d=e[2].toUpperCase(),e[1].length>0?(c[d]=e[1],u(a)):u(c.hasOwnProperty(d)?c[d]+e[2]+e[3]:b.hasOwnProperty(d)?b[d]+e[2]+e[3]:a)):a}function w(a){var b,c,d,e=/^(\d*)(?:\/(\d*))?$|^(\/+)$/.exec(a),f=0;if(e){if(e[3])return Math.pow(.5,e[3].length);if(c=e[2]?parseFloat(e[2]):/\//.test(a)?2:1,d=0,b=e[1]?parseFloat(e[1]):1,e[2])for(;d+1<e[1].length&&b>c;)d+=1,f=parseFloat(e[1].substring(0,d)),b=parseFloat(e[1].substring(d));return f+b/c}}var x,y,z,A,B=a.split("\n"),C={},D=C,E={},F={slurred:0};for(x=0;x<B.length;++x)if(z=f.exec(B[x]))b(z[1],z[2].trim());else{if(/^\s*(?:%.*)?$/.test(B[x]))continue;h(B[x])}var G=["unitnote","unitbeat","tempo"];if(C.voice){A=[];for(x in C.voice)if(C.voice[x].stems&&C.voice[x].stems.length){for(l(C.voice[x].stems),y=0;y<G.length;++y)!(G[y]in C)&&G[y]in C.voice[x]&&(C[G[y]]=C.voice[x][G[y]]);delete C.voice[x].accent}else A.push(x);for(x=0;x<A.length;++x)delete C.voice[A[x]]}return C}},{"./utils":4}],4:[function(a,b,c){(function(c){function d(){if(getAudioTop.audioTop){var a=getAudioTop.audioTop;a.out&&(a.out.disconnect(),a.out=null,a.currentStart=null);var b=a.ac.createDynamicsCompressor();b.ratio=16,b.attack=5e-4,b.connect(a.ac.destination),a.out=b}}function e(){if(null==c)for(var a=getAudioTop().ac,b=2*a.sampleRate,c=a.createBuffer(1,b,a.sampleRate),d=c.getChannelData(0),e=0;b>e;e++)d[e]=2*Math.random()-1;return c}var f=a("./wavetable-builder");b.exports.isAudioPresent=isAudioPresent=function(){return!(!c.AudioContext&&!c.webkitAudioContext)},b.exports.getAudioTop=getAudioTop=function(){if(getAudioTop.audioTop)return getAudioTop.audioTop;if(!isAudioPresent())return null;var a=new(c.AudioContext||c.webkitAudioContext);return getAudioTop.audioTop={ac:a,wavetable:f(a),out:null,currentStart:null},d(),getAudioTop.audioTop},b.exports.audioCurrentStartTime=audioCurrentStartTime=function(){var a=getAudioTop();return null!=a.currentStart?a.currentStart:(a.currentStart=Math.max(.25,a.ac.currentTime),setTimeout(function(){a.currentStart=null},0),a.currentStart)},b.exports.midiToFrequency=midiToFrequency=function(a){return 440*Math.pow(2,(a-69)/12)};var g={C:0,D:2,E:4,F:5,G:7,A:9,B:11,c:12,d:14,e:16,f:17,g:19,a:21,b:23},h={"^":1,"":0,"=":0,_:-1},i=["C","^C","D","_E","E","F","^F","G","_A","A","_B","B","c","^c","d","_e","e","f","^f","g","_a","a","_b","b"];b.exports.pitchToMidi=pitchToMidi=function(a){var b=/^(\^+|_+|=|)([A-Ga-g])([,']*)$/.exec(a);if(!b)return null;var c=b[3].replace(/,/g,"").length-b[3].replace(/'/g,"").length,d=g[b[2]]+h[b[1].charAt(0)]*b[1].length+12*c;return d+60},b.exports.midiToPitch=function(a){var b=(a-72)%12;(a>60||0!=b)&&(b+=12);for(var c=Math.round((a-b-60)/12),d=i[b];0!=c;)d+=c>0?"'":",",c+=c>0?-1:1;return d},b.exports.pitchToFrequency=pitchToFrequency=function(a){return midiToFrequency(pitchToMidi(a))},b.exports.defaultTimbre=defaultTimbre={wave:"square",gain:.1,attack:.002,decay:.4,decayfollow:0,sustain:0,release:.1,cutoff:0,cutfollow:0,resonance:0,detune:0},b.exports.makeTimbre=function(a,b){a||(a={}),"string"==typeof a&&(a={wave:a});var c,d={},e=b&&b.wavetable&&b.wavetable[a.wave];for(c in defaultTimbre)a.hasOwnProperty(c)?d[c]=a[c]:e&&e.defs&&e.defs.hasOwnProperty(c)?d[c]=e.defs[c]:d[c]=defaultTimbre[c];return d};b.exports.makeOscillator=makeOscillator=function(a,b,c){if("noise"==b){var d=a.ac.createBufferSource();return d.buffer=e(),d.loop=!0,d}var f,g,h,i,j=a.wavetable,k=a.ac.createOscillator();try{if(j.hasOwnProperty(b)){if(g=j[b].wave,j[b].freq){h=0;for(f in j[b].freq)i=Number(f),c>i&&i>h&&(h=i,g=j[b].freq[h])}!k.setPeriodicWave&&k.setWaveTable?k.setWaveTable(g):k.setPeriodicWave(g)}else k.type=b}catch(l){window.console&&window.console.log(l),k.type="square"}return k.frequency.value=c,k}}).call(this,"undefined"!=typeof global?global:"undefined"!=typeof self?self:"undefined"!=typeof window?window:{})},{"./wavetable-builder":5}],5:[function(a,b,c){b.exports=function(a){return function(b){function c(b){var c,d=b.real.length,e=new Float32Array(d),f=new Float32Array(d);for(c=0;d>c;++c)e[c]=b.real[c],f[c]=b.imag[c];try{return a.createPeriodicWave(e,f)}catch(g){}try{return a.createWaveTable(e,f)}catch(g){}return null}function d(a,b,c){var d,e,f={real:[],imag:[]},g=a.real.length;for(d=0;g>d;++d)e=Math.log(b[Math.min(d,b.length-1)]),f.real.push(a.real[d]*Math.exp(c*e)),f.imag.push(a.imag[d]*Math.exp(c*e));return f}var e,f,g,h,i,j,k={};for(e in b)if(f=b[e],j=c(f)){if(i={wave:j},f.mult)for(h=b[e].freq,i.freq={},g=0;g<h.length;++g)j=c(d(f,f.mult,(g+1)/h.length)),j&&(i.freq[h[g]]=j);f.defs&&(i.defs=f.defs),k[e]=i}return k}({piano:{real:[0,0,-.203569,.5,-.401676,.137128,-.104117,.115965,-.004413,.067884,-.00888,.0793,-.038756,.011882,-.030883,.027608,-.013429,.00393,-.014029,.00972,-.007653,.007866,-.032029,.046127,-.024155,.023095,-.005522,.004511,-.003593,.011248,-.004919,.008505],imag:[0,.147621,0,7e-6,-1e-5,5e-6,-6e-6,9e-6,0,8e-6,-1e-6,14e-6,-8e-6,3e-6,-9e-6,9e-6,-5e-6,2e-6,-7e-6,5e-6,-5e-6,5e-6,-23e-6,37e-6,-21e-6,22e-6,-6e-6,5e-6,-4e-6,14e-6,-7e-6,12e-6],mult:[1,1,.18,.016,.01,.01,.01,.004,.014,.02,.014,.004,.002,1e-5],freq:[65,80,100,135,180,240,620,1360],defs:{wave:"piano",gain:.5,attack:.002,decay:.25,sustain:.03,release:.1,decayfollow:.7,cutoff:800,cutfollow:.1,resonance:1,detune:.9994}}})}},{}]},{},[1])(1)});