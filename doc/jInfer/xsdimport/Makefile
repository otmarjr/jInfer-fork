default: all

xsdimport.dvi: parser_intf.eps

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
	dot -Tpng <$ > $(@:.eps=.png)

all: xsdimport.pdf

clean:
	rm -f *.blg *.log *.aux *.dvi *.toc *.bbl .*blg *.slo *.srs *.lof *.haux

web:	clean
	rm -f *.png *.eps

dist-clean: web
	rm -f *.html *.pdf

booklet: main.ps
	cat main.ps | psbook | psnup -2 >main-booklet.ps

