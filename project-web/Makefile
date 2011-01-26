CPP=/usr/bin/cpp -P -w -C -undef

.PHONY: images photos style pdfs outputdir

all: outputdir about.html doc_tutorial.html documentation.html download.html empty.html footer.html header.html help.html index.html legal.html makingof.html building_jinfer.html glossary.html screenshots.html doc_tutorial_dev.html doc_tutorial_platform.html images photos style pdfs htaccess screenshots

IMGS= jinfer-logo.png document-save-2.png edit-find-4.png configure-4.png help-hint.png background3.png box-downleft.png

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
	mkdir -p output
	cp .htaccess output/.htaccess

pdfs:
	mkdir -p output/modules
	for i in `ls modules/*.pdf`; do cp $$i output/$$i; done

%.html: %.html_
	$(CPP) $< > output/$@

screenshots:
	mkdir -p output/images/screenshots/thumbs
	for i in `ls images/screenshots/`; do convert -resize 320 images/screenshots/$$i output/images/screenshots/thumbs/$$i; done
	for i in `ls images/screenshots/`; do cp images/screenshots/$$i output/images/screenshots/; done

clean:
	rm -fr output
