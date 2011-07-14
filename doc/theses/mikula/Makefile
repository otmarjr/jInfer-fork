all: diplomka.pdf

# Vyroba PDF primo z DVI by byla prijemnejsi, ale vetsina verzi dvipdfm nici obrazky
# prace.pdf: prace.dvi
#	dvipdfm -o $@ -p a4 -r 600 $<

diplomka.pdf: diplomka.ps
	ps2pdf $< $@

diplomka.ps: diplomka.dvi
	dvips -o $@ -D600 -t a4 $<

# LaTeX je potreba spustit dvakrat, aby spravne spocital odkazy
diplomka.dvi: diplomka.tex *.bib $(wildcard *.tex)
	rm -f *.toc
	latex diplomka
	bibtex diplomka
	latex diplomka
	latex diplomka

clean:
	rm -f *.{log,out,aux,toc,bbl,blg,slo,srs}

dist-clean:
	rm -f *.{log,dvi,aux,toc,lot,out,bbl,blg,slo,srs} diplomka.ps diplomka.pdf
