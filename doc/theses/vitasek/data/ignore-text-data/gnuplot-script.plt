set style data boxplot

set ylabel 'Time [ms]'

set yrange [ 5000 : 11000 ]
set xrange [ 0 : 5 ]

unset key

set style fill solid 0.25 border lt -1

set pointsize 0.5

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics norangelimit
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0

set xtics ("GE" 1, "GE-ignore" 2, "MC" 3, "MC-ignore" 4)

plot for [i = 1 : 4] 'result.txt' using (i) : i lt (i % 2 + 1)
