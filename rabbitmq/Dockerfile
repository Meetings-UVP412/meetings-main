FROM rabbitmq:3.13-management

RUN apt-get update && \
    apt-get install -y curl && \
    curl -L -o /opt/rabbitmq/plugins/rabbitmq_delayed_message_exchange-3.13.0.ez \
        https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases/download/v3.13.0/rabbitmq_delayed_message_exchange-3.13.0.ez && \
    rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange && \
    apt-get remove -y curl && \
    apt-get autoremove -y && \
    rm -rf /var/lib/apt/lists/*

COPY enabled_plugins /etc/rabbitmq/enabled_plugins