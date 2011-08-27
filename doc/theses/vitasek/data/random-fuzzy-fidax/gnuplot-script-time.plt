set style data boxplot

set xlabel 'Data set'
set ylabel 'Time'

set yrange [ 1 : 10000 ]
set xrange [ 0 : 34 ]

unset key

set style fill solid 0.25 border lt -1

set pointsize 0.5

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics norangelimit
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0

set xtics ("OVA1" 2, "OVA2" 5, "OVA3" 8, "XMA-c" 11, "XMA-p" 14, "XMD" 17, "MSH" 20, "NTH" 23, "100-100" 26, "100-200" 29, "100-1000" 32)

set logscale y 10

plot for [i = 1 : 33] 'result-time.txt' using (i) : i lt (i % 3 == 0 ? 1 : (i % 3 == 1 ? 2 : 3))
