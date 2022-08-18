def lambda_handler(event, context):
    global bucket
    global key_name

    loop_count = 1
    for record in event['Records']:

        message_full_body = record['body']
        message_full_body_dict = json.loads(message_full_body)
        m1 = message_full_body_dict['Message']
        m2 = json.loads(m1)

        try:
            bucket = m2['Records'][0]["s3"]["bucket"]["name"]
            key_name = m2['Records'][0]["s3"]["object"]["key"]

        except Exception as e:
            print(str(e))
        else:

            obj = s3.get_object(Bucket=bucket, Key=key_name)
            df = pd.read_csv(io.BytesIO(obj['Body'].read()))