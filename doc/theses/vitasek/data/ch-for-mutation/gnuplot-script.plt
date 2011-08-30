set style data boxplot

set ylabel 'Quality'

set yrange [ .3 : .9 ]
set xrange [ 0 : 13 ]

unset key

set style fill solid 0.25 border lt -1

set pointsize 0.5

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics norangelimit
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0

set xtics ("80-320" 2, "90-405" 4, "100-500" 6, "100-100" 8, "100-200" 10, "100-1000" 12)

plot for [i = 1 : 12] 'result.txt' using (i + 0.5) : i lt (i % 2 + 1)
