FROM ubuntu:22.10

RUN apt-get update && \
    apt-get install -y wget sudo gnupg unzip xvfb curl
RUN apt-get update && \
    apt install -y openjdk-11-jdk
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | sudo apt-key add - && \
    echo "deb https://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list && \
    apt-get update && \
    apt-get install --yes google-chrome-stable && \
    CHROME_VERSION=$(google-chrome --product-version | grep -Po '.*(?=\.)') && \
    CHROME_DRIVER_VERSION=$(curl -s "https://chromedriver.storage.googleapis.com/LATEST_RELEASE_$CHROME_VERSION") && \
    wget https://chromedriver.storage.googleapis.com/$CHROME_DRIVER_VERSION/chromedriver_linux64.zip && \
    unzip chromedriver_linux64.zip && \
    mv chromedriver /usr/bin/chromedriver && \
    chown root:root /usr/bin/chromedriver && \
    chmod +x /usr/bin/chromedriver \
