if [ ! -d "localstack" ]; then
  echo "localstack not installed -> DOWNLOADING it"
  wget https://github.com/localstack/localstack-cli/releases/download/v3.3.0/localstack-cli-3.3.0-linux-amd64.tar.gz
  tar zxvf localstack-cli-3.3.0-linux-amd64.tar.gz
  rm localstack-cli-3.3.0-linux-amd64.tar.gz
fi

./localstack/localstack start &