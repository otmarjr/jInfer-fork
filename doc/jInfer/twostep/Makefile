default: all

twostep.dvi: twostep_modules.eps automaton_merging_state_modules.eps twostep_modules_complete.eps factory_pattern1.eps factory_pattern2.eps state_removal_modules.eps

%.dvi: %.tex literature.bib Makefile
	rm -f *.toc
	latex $< 
	bibtex $(<:.tex=.aux)
	latex $<
	latex $< 

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

all: twostep.pdf

clean:
	rm -f *.blg *.log *.aux *.dvi *.toc *.bbl .*blg *.slo *.srs *.lof *.haux
dist-clean:
	rm -f *.blg *.log *.aux *.dvi *.toc *.bbl .*blg *.slo *.srs *.html *.lof *.haux *.pdf *.eps

booklet: main.ps
	cat main.ps | psbook | psnup -2 >main-booklet.ps

