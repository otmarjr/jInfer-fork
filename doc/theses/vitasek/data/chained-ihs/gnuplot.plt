unset key
set style data lines

set border 3 front linetype -1 linewidth 1
set xtics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0
set xtics norangelimit
set ytics border in scale 1, 0.5 nomirror norotate offset character 0, 0, 0

set xlabel "Time [ms]"
set ylabel "Quality"

plot 'result-2-100-100.csv' using 1:2 lt 1, '' using 3:4 lt 1, '' using 5:6 lt 1, '' using 7:8 lt 1, '' using 9:10 lt 1, '' using 11:12 lt 1, '' using 13:14 lt 1, '' using 15:16 lt 1, '' using 17:18 lt 1, '' using 19:20 lt 1
