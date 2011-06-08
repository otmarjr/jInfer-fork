obj= architecture base basicdtd basicigg basicruledisplayer basicxsd runner twostep treeruledisplayer autoeditor projecttype xsdimport paper

all: deploy

deploy: subdirs
	mkdir -p modules/;
	for i in $(obj); do cp $$i/$$i.pdf modules/; done

upload: deploy
	rsync --delete -r modules/ svirooo,jinfer@web.sourceforge.net:htdocs/modules/

subdirs:
	for i in $(obj); do cd $$i && $(MAKE) && cd ..; done

clean:
	for i in $(obj); do cd $$i && $(MAKE) clean && cd ..; done

dist-clean:
	for i in $(obj); do cd $$i && $(MAKE) dist-clean && cd ..; done
