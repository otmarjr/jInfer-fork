set style data boxplot

set xlabel 'Algorithm'
set ylabel 'Time [ms]'

set yrange [ 1 : 100000 ]
set xrange [ 0.5 : 2.5 ]

unset key

set style fill solid 0.25 border lt -1

set pointsize 0.5

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics ("Glpk" 1, "Tuned Strategy 1" 2)
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0

plot for [i = 1 : 2] 'result.txt' using (i):i lt (i % 2 == 0 ? 1 : 2)
