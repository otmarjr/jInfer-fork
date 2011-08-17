set border 2 front linetype -1 linewidth 1.000
set boxwidth 0.5 absolute
set style fill   solid 0.25 border lt -1
unset key
# unset xtics
# set key autotitle columnhead
set pointsize 0.5
set style data boxplot
set xtics border in scale 0,0 nomirror norotate  offset character 0, 0, 0
set xtics norangelimit
#set for [i = 11 : 1 : -1] xtics add ((i % 2 == 0 ? "R" : "F") i)
set ytics border in scale 1,0.5 nomirror norotate  offset character 0, 0, 0
set yrange [ .44 : .5 ] noreverse nowriteback
set xrange [ 0 : 51 ] noreverse nowriteback
plot for [i = 1 : 50] 'result.txt' using (i):i lt (i % 2 == 0 ? 1 : 2)
