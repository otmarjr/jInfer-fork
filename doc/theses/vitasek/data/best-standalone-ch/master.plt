################################
reset
set term postscript eps enhanced
set output "100-1000.eps"
################################

set title 'Best Construction Heuristic - 100-1000'

set style data boxplot

set xlabel 'Construction Heuristic'
set ylabel 'Quality'

set yrange [ .0 : .4 ]
set xrange [ 0 : 7 ]

unset key

set style fill solid 0.25 border lt -1

set pointsize 0.5

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics norangelimit
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0

set xtics ("Random" 1, "Fuzzy" 2, "Incremental" 3, "Removal" 4, "FIDAX" 5, "Glpk" 6)

plot for [i = 1 : 6] 'result-100-1000.txt' using (i):i ls 1

################################
reset
set term postscript eps enhanced
set output "100-100.eps"
################################

set title 'Best Construction Heuristic - 100-100'

set style data boxplot

set xlabel 'Construction Heuristic'
set ylabel 'Quality'

set yrange [ .1 : .9 ]
set xrange [ 0 : 7 ]

unset key

set style fill solid 0.25 border lt -1

set pointsize 0.5

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics norangelimit
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0

set xtics ("Random" 1, "Fuzzy" 2, "Incremental" 3, "Removal" 4, "FIDAX" 5, "Glpk" 6)

plot for [i = 1 : 6] 'result-100-100.txt' using (i):i ls 1

################################
reset
set term postscript eps enhanced
set output "100-200.eps"
################################

set title 'Best Construction Heuristic - 100-200'

set style data boxplot

set xlabel 'Construction Heuristic'
set ylabel 'Quality'

set yrange [ .1 : .8 ]
set xrange [ 0 : 7 ]

unset key

set style fill solid 0.25 border lt -1

set pointsize 0.5

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics norangelimit
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0

set xtics ("Random" 1, "Fuzzy" 2, "Incremental" 3, "Removal" 4, "FIDAX" 5, "Glpk" 6)

plot for [i = 1 : 6] 'result-100-200.txt' using (i):i ls 1

################################
reset
set term postscript eps enhanced
set output "MSH.eps"
################################

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

plot for [i = 1 : 6] 'result-MSH.txt' using (i):i ls 1

################################
reset
set term postscript eps enhanced
set output "NTH.eps"
################################

set title 'Best Construction Heuristic - NTH'

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

plot for [i = 1 : 6] 'result-NTH.txt' using (i):i ls 1

################################
reset
set term postscript eps enhanced
set output "OVA1.eps"
################################

set title 'Best Construction Heuristic - OVA1'

set style data boxplot

set xlabel 'Construction Heuristic'
set ylabel 'Quality'

set yrange [ .25 : .5 ]
set xrange [ 0 : 7 ]

unset key

set style fill solid 0.25 border lt -1

set pointsize 0.5

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics norangelimit
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0

set xtics ("Random" 1, "Fuzzy" 2, "Incremental" 3, "Removal" 4, "FIDAX" 5, "Glpk" 6)

plot for [i = 1 : 6] 'result-OVA1.txt' using (i):i ls 1

################################
reset
set term postscript eps enhanced
set output "OVA2.eps"
################################

set title 'Best Construction Heuristic - OVA2'

set style data boxplot

set xlabel 'Construction Heuristic'
set ylabel 'Quality'

set yrange [ .05 : .18 ]
set xrange [ 0 : 7 ]

unset key

set style fill solid 0.25 border lt -1

set pointsize 0.5

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics norangelimit
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0

set xtics ("Random" 1, "Fuzzy" 2, "Incremental" 3, "Removal" 4, "FIDAX" 5, "Glpk" 6)

plot for [i = 1 : 6] 'result-OVA2.txt' using (i):i ls 1

################################
reset
set term postscript eps enhanced
set output "OVA3.eps"
################################

set title 'Best Construction Heuristic - OVA3'

set style data boxplot

set xlabel 'Construction Heuristic'
set ylabel 'Quality'

set yrange [ .24 : .26 ]
set xrange [ 0 : 7 ]

unset key

set style fill solid 0.25 border lt -1

set pointsize 0.5

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics norangelimit
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0

set xtics ("Random" 1, "Fuzzy" 2, "Incremental" 3, "Removal" 4, "FIDAX" 5, "Glpk" 6)

plot for [i = 1 : 6] 'result-OVA3.txt' using (i):i ls 1

################################
reset
set term postscript eps enhanced
set output "XMA-c.eps"
################################

set title 'Best Construction Heuristic - XMA-c'

set style data boxplot

set xlabel 'Construction Heuristic'
set ylabel 'Quality'

set yrange [ .7 : .8 ]
set xrange [ 0 : 7 ]

unset key

set style fill solid 0.25 border lt -1

set pointsize 0.5

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics norangelimit
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0

set xtics ("Random" 1, "Fuzzy" 2, "Incremental" 3, "Removal" 4, "FIDAX" 5, "Glpk" 6)

plot for [i = 1 : 6] 'result-XMA-c.txt' using (i):i ls 1

################################
reset
set term postscript eps enhanced
set output "XMA-p.eps"
################################

set title 'Best Construction Heuristic - XMA-p'

set style data boxplot

set xlabel 'Construction Heuristic'
set ylabel 'Quality'

set yrange [ .1 : .3 ]
set xrange [ 0 : 7 ]

unset key

set style fill solid 0.25 border lt -1

set pointsize 0.5

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics norangelimit
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0

set xtics ("Random" 1, "Fuzzy" 2, "Incremental" 3, "Removal" 4, "FIDAX" 5, "Glpk" 6)

plot for [i = 1 : 6] 'result-XMA-p.txt' using (i):i ls 1

################################
reset
set term postscript eps enhanced
set output "XMD.eps"
################################

set title 'Best Construction Heuristic - XMD'

set style data boxplot

set xlabel 'Construction Heuristic'
set ylabel 'Quality'

set yrange [ .075 : .1 ]
set xrange [ 0 : 7 ]

unset key

set style fill solid 0.25 border lt -1

set pointsize 0.5

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics norangelimit
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0

set xtics ("Random" 1, "Fuzzy" 2, "Incremental" 3, "Removal" 4, "FIDAX" 5, "Glpk" 6)

plot for [i = 1 : 6] 'result-XMD.txt' using (i):i
