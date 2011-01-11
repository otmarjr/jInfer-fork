default: all

architecture.dvi: nodes.eps interfaces_nodes.eps reg_tree.eps

%.dvi: %.tex *.eps *.bib Makefile
	rm -f *.toc
	latex $< 
	bibtex $(<:.tex=.aux)
	latex $<
	latex $< 

%.pdf: %.dvi
	dvipdfmx $<

%.html: %.tex *.bib Makefile
	rm -f *.toc
	hevea -O -pedantic $<
	bibtex $(<:.tex=.aux)
	hevea -O -pedantic $<
	hevea -O -pedantic $<

%.eps: %.gv Makefile
	dot -Teps $< > $@
	epstopdf $@

all: architecture.pdf

clean:
	rm -f *.blg *.log *.aux *.dvi *.toc *.bbl .*blg *.slo *.srs *.lof *.haux
dist-clean:
	rm -f *.blg *.log *.aux *.dvi *.toc *.bbl .*blg *.slo *.srs *.html *.lof *.haux *.pdf *.eps

booklet: main.ps
	cat main.ps | psbook | psnup -2 >main-booklet.ps

