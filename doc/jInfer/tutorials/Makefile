default: all

%.dvi: %.tex Makefile
	rm -f *.toc
	latex -interaction=nonstopmode $< 
	latex -interaction=nonstopmode $<
	latex -interaction=nonstopmode $< 

%.pdf: %.dvi
	dvipdfmx $<

all: building_jinfer.pdf doc_tutorial.pdf doc_tutorial_dev.pdf doc_tutorial_platform.pdf

clean:
	rm -f *.blg *.log *.aux *.dvi *.toc *.bbl .*blg *.slo *.srs *.lof *.haux
dist-clean:
	rm -f *.blg *.log *.aux *.dvi *.toc *.bbl .*blg *.slo *.srs *.html *.lof *.haux *.pdf *.eps

