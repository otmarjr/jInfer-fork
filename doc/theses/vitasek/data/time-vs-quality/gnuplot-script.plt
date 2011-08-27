set style data boxplot
set boxwidth 1.5 absolute

set xlabel 'Time limit [s]'
set ylabel 'Quality'

set yrange [ .43 : .5 ]
set xrange [ 0 : 47 ]

unset key

set style fill solid 0.25 border lt -1

set pointsize 0.5

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics norangelimit
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0



plot for [i = 1 : 20] 'result.txt' using ((i / 2) * 5 + 1 + (i % 2) * 2.5) : i lt (i % 2 == 0 ? 1 : 2)
