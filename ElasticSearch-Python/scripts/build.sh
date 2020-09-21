#!/bin/bash
cd `pwd`/../pyinstaller-3.1
rm -rf ElasticSearch-Python
python pyinstaller.py -F ../ElasticSearch-Python.py
yes|cp ElasticSearch-Python/dist/ElasticSearch-Python ..
cd ../..
echo `pwd`
version=`head -n2 ElasticSearch-Python/version | tail -n1 | cut -d'=' -f2`
rm -rf ElasticSearch-Python-${version}-GEN
mkdir -p ElasticSearch-Python-${version}-GEN
cp -rf ElasticSearch-Python ElasticSearch-Python-${version}-GEN/
cp ElasticSearch-Python/scripts/install.sh ElasticSearch-Python-${version}-GEN/
cp ElasticSearch-Python/scripts/uninstall.sh ElasticSearch-Python-${version}-GEN/
tar -czvf ElasticSearch-Python-${version}-GEN.tar.gz ElasticSearch-Python-${version}-GEN/
echo "build in `pwd`/ElasticSearch-Python-${version}-GEN.tar.gz"
