default: all

architecture.dvi: nodes.eps nodes_full.eps interfaces_nodes.eps inference_process.eps reg_tree.eps xml_example.eps automaton_merge1.eps automaton_merge2.eps \
automaton_merge3.eps nodes_full2.eps module_deps.eps nodes.eps factory_pattern1.eps factory_pattern2.eps

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

all: architecture.pdf

clean:
	rm -f *.blg *.log *.aux *.dvi *.toc *.bbl .*blg *.slo *.srs *.lof *.haux
dist-clean:
	rm -f *.blg *.log *.aux *.dvi *.toc *.bbl .*blg *.slo *.srs *.html *.lof *.haux *.pdf *.eps *.png

booklet: main.ps
	cat main.ps | psbook | psnup -2 >main-booklet.ps

