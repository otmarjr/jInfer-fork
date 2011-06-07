all: prace.pdf

# Vyroba PDF primo z DVI by byla prijemnejsi, ale vetsina verzi dvipdfm nici obrazky
# prace.pdf: prace.dvi
#	dvipdfm -o $@ -p a4 -r 600 $<

prace.pdf: prace.ps
	ps2pdf $< $@

prace.ps: prace.dvi
	dvips -o $@ -D600 -t a4 $<

# LaTeX je potreba spustit dvakrat, aby spravne spocital odkazy
prace.dvi: prace.tex *.bib $(wildcard *.tex)
	rm -f *.toc
	latex prace
	bibtex prace
	latex prace
	latex prace

clean:
	rm -f *.{log,out,aux,toc,bbl,blg,slo,srs}

dist-clean:
	rm -f *.{log,dvi,aux,toc,lof,out,bbl,blg,slo,srs} prace.ps prace.pdf
