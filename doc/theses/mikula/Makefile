default: all

%.dvi: %.tex literature.bib Makefile
	rm -f *.toc
	latex -interaction=nonstopmode $< 
	bibtex $(<:.tex=.aux)
	latex -interaction=nonstopmode $<
	latex -interaction=nonstopmode $< 

%.pdf: %.dvi
	dvipdfmx $<

%.html: %.tex literature.bib Makefile
	rm -f *.toc
	hevea -O -pedantic $<
	bibtex $(<:.tex=.aux)
	hevea -O -pedantic $<
	hevea -O -pedantic $<

%.eps: %.gv Makefile
	dot -Teps $< > $@
	dot -Tpng $< > $(@:.eps=.png)

all: diplomka.pdf

clean:
	rm -f *.blg *.log *.aux *.dvi *.toc *.bbl .*blg *.slo *.srs *.lof *.haux *.pdf
dist-clean:
	rm -f *.blg *.log *.aux *.dvi *.toc *.bbl .*blg *.slo *.srs *.html *.lof *.haux *.pdf 
