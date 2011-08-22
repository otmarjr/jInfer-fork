set title 'Best Construction Heuristic - MSH'

set style data boxplot

set xlabel 'Construction Heuristic'
set ylabel 'Quality'

set yrange [ .0 : 1 ]
set xrange [ 0 : 7 ]

unset key

set style fill solid 0.25 border lt -1

set pointsize 0.5

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics norangelimit
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0

set xtics ("Random" 1, "Fuzzy" 2, "Incremental" 3, "Removal" 4, "FIDAX" 5, "Glpk" 6)

plot for [i = 1 : 6] 'result-MSH.txt' using (i):i
