set style data boxplot
set boxwidth .8 absolute

set xlabel 'Time limit [s]'
set ylabel 'Quality'

set yrange [ .43 : .5 ]
set xrange [ 0 : 21 ]

unset key

set style fill solid 0.25 border lt -1

set pointsize 0.5

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics norangelimit
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0

set xtics ("N 1" 1, "C 1" 2, "N 6" 3, "C 6" 4, "N 11" 5, "C 11" 6, "N 16" 7, "C 16" 8, "N 21" 9, "C 21" 10, "N 26" 11, "C 26" 12, "N 31" 13, "C 31" 14, "N 36" 15, "C 36" 16, "N 41" 17, "C 41" 18, "N 46" 19, "C 46" 20)

plot for [i = 1 : 20] 'result.txt' using (i) : i lt (i % 2 == 0 ? 1 : 2)
