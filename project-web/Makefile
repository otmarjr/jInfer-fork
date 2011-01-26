CPP=/usr/bin/cpp -P -w -C -undef

.PHONY: images photos style pdfs outputdir

all: outputdir about.html doc_tutorial.html documentation.html download.html empty.html footer.html header.html help.html index.html legal.html makingof.html building_jinfer.html glossary.html screenshots.html doc_tutorial_dev.html doc_tutorial_platform.html images photos style pdfs htaccess

IMGS= jinfer-logo.png document-save-2.png edit-find-4.png configure-4.png help-hint.png background3.png box-downleft.png screen-hudson.png screen-statsvn.png screen-validator.png screen-1-overall.png screen-5-properties.png screen-2-advacedruledisplayer.png screen-6-result.png screen-3-autoeditor.png screen-4-autoeditor-complex.png

PHTS= anti.jpg reseto.jpg rio.jpg sviro.jpg vektor.jpg

outputdir:
	mkdir -p output

images:
	mkdir -p output/images
	for i in $(IMGS); do cp images/$$i output/images/$$i; done

photos:
	mkdir -p output/photos
	for i in $(PHTS); do cp photos/$$i output/photos/$$i; done

style:
	cp style.css output/style.css

htaccess:
	cp .htaccess /output/.htaccess

pdfs:
	mkdir -p output/modules
	cp modules/*.pdf output/modules/

%.html: %.html_
	$(CPP) $< > output/$@


clean:
	rm -fr output
