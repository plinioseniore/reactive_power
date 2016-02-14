// Import data

dir = 'C:\reactive_power\';

t=read_csv(dir + 't.csv');
vac=read_csv(dir + 'vac.csv');
iac=read_csv(dir + 'iac.csv');
vdc=read_csv(dir + 'vdc.csv');
idc=read_csv(dir + 'idc.csv');


t=strtod(t);
vac=strtod(vac);
iac=strtod(iac);
vdc=strtod(vdc);
idc=strtod(idc);

// Sampling rate
sampling=4000;

// Start of first 50 Hz period of VAC
k1=2042;
k2=k1+sampling;

// Instant power
pac=vac.*iac;
pdc=vdc.*idc;

// Average power as integral of p in t(k1) to t(k2)
function p_avr = power_avg(t, p, k1, k2)
	k=k1;
	p_avr = 0;
	while (k <= k2)
		p_avr = p_avr + p(k)*(t(k)-t(k-1));
		k=k+1;
	end
	p_avr = p_avr/(t(k2)-t(k1));
endfunction

// RMS value of i in t(k1) to t(k2)
function i_rms = rms_avg(t, i, k1, k2)
	k=k1;
	i_rms = 0;
	while (k <= k2)
		i_rms = i_rms + i(k)*i(k)*(t(k)-t(k-1));
		k=k+1;
	end
	i_rms = i_rms/(t(k2)-t(k1));
	i_rms = sqrt(i_rms);
endfunction

// Average and RMS values
pac_avr=power_avg(t,pac,k1,k2);
pdc_avr=power_avg(t,pdc,k1,k2);
iac_rms=rms_avg(t,iac,k1,k2);
vac_rms=rms_avg(t,vac,k1,k2);

// AC Power Factor
p_factor = pac_avr/(vac_rms*iac_rms);

// Plot
scf();
subplot(1,3,1);
plot(t(k1:k2),vac(k1:k2));
xtitle('AC Voltage','s', 'V');
xgrid;

subplot(1,3,2);
plot(t(k1:k2),iac(k1:k2));
xtitle('AC Current','s', 'A');
xgrid;

subplot(1,3,3);
plot(t(k1:k2),pac(k1:k2));
xtitle('AC Instant Power, AC Average Power = ' + string(pac_avr) + ' W , AC Power Factor = ' + string(p_factor),'ms', 'W');
xgrid;

scf();
subplot(1,3,1);
plot(t(k1:k2),vdc(k1:k2));
xtitle('DC Voltage','s', 'V');
xgrid;

subplot(1,3,2);
plot(t(k1:k2),idc(k1:k2));
xtitle('DC Current','s', 'A');
xgrid;

subplot(1,3,3);
plot(t(k1:k2),pdc(k1:k2));
xtitle('DC Instant Power','ms', 'W');
xgrid;
