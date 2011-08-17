set title 'Time until Optimum'

set style data boxplot
set boxwidth 4 absolute

set xlabel 'TODO'
set ylabel 'Time [ms]'

set yrange [ 1 : 100000 ]
set xrange [ -10 : 110 ]

unset key

set style fill solid 0.25 border lt -1

set pointsize 0.5

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics norangelimit
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0

set logscale y 10

plot for [i = 1 : 22] 'result.txt' using ((i / 2.0 - 1) * 10 + 5):i lt (i % 2 == 0 ? 1 : 2)