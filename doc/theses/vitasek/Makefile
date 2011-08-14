default: all

IMAGESTOCONVERT=$(shell cat diplomka.tex | grep includegraphics | sed -e "s/.*includegraphics\[.*\]{\([^}]*\)}.*/\1\.pdf/")

%.dvi: %.tex literature.bib Makefile
	rm -f *.toc
	latex -interaction=nonstopmode $< 
	bibtex $(<:.tex=.aux)
	latex -interaction=nonstopmode $<
	latex -interaction=nonstopmode $< 

%.pdf: %.eps
	epstopdf $<

all: ${IMAGESTOCONVERT} literature.bib Makefile diplomka.tex
	rm -f *.toc
	pdflatex -interaction=nonstopmode diplomka
	pdflatex -interaction=nonstopmode diplomka
	bibtex diplomka
	pdflatex -interaction=nonstopmode diplomka

clean:
	rm -f *.blg *.log *.aux *.dvi *.toc *.bbl .*blg *.slo *.srs *.lof *.haux *.pdf *.lot *.loa *.nlo *.out *.ps ${IMAGESTOCONVERT}
dist-clean:
	rm -f *.blg *.log *.aux *.dvi *.toc *.bbl .*blg *.slo *.srs *.html *.lof *.haux *.pdf *.lot *.loa *.nlo *.out *.ps
