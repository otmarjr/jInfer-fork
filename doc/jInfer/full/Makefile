default: all

main.dvi: *.tex *.bib Makefile
	rm -f *.toc
	latex main
	bibtex main
	latex main
	latex main

main.ps: main.dvi
	dvips main.dvi

main.pdf: *.tex *.bib Makefile
	rm -f *.toc
	latex main
	bibtex main
	latex main
	latex main
	dvipdfmx main

main.html: *.tex *.bib Makefile
	rm -f *.toc
	hevea main
	bibtex main
	hevea main
	hevea main

dvi: main.dvi

ps: main.ps

pdf: main.pdf

html: main.html

all: pdf ps html

clean:
	rm -f *.blg *.log *.aux *.dvi *.toc *.bbl .*blg *.slo *.srs *.html *.lof *.haux main.ps main.pdf
dist-clean:
	rm -f *.blg *.log *.aux *.dvi *.toc *.bbl .*blg *.slo *.srs *.html *.lof *.haux main.ps main.pdf

booklet: main.ps
	cat main.ps | psbook | psnup -2 >main-booklet.ps

