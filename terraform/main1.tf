provider "aws" {

  access_key = ""
  secret_key = ""


  region = "eu-central-1"
}

resource "aws_instance" "iiot_poc_demo_1" {
    ami = "ami-1c45e273"
    instance_type = "t2.micro"
    vpc_security_group_ids = ["${aws_security_group.iiot_poc_demo_sg_1.id}"]

  key_name = "iiotkey"

  tags {
    Name = "iiot.poc.demo.1"
  }

  provisioner "file" {
    source      = "target/.jar"
    destination = "/home/ubuntu/.jar"

    connection {
      user = "ubuntu"
      private_key = "${file("c:/Program Files (x86)/PuTTY/iiotkey.pem")}"
      agent = false
    }
  }

  provisioner "file" {
    source      = "target/.jar"
    destination = "/home/ubuntu/.jar"

    connection {
      user = "ubuntu"
      private_key = "${file("c:/Program Files (x86)/PuTTY/iiotkey.pem")}"
      agent = false
    }
  }

  provisioner "file" {
    source      = "target/ServerExample1.der"
    destination = "/home/ubuntu/ServerExample1.der"

    connection {
      user = "ubuntu"
      private_key = "${file("c:/Program Files (x86)/PuTTY/iiotkey.pem")}"
      agent = false
    }
  }

  provisioner "file" {
    source      = "target/ServerExample1.pem"
    destination = "/home/ubuntu/ServerExample1.pem"

    connection {
      user = "ubuntu"
      private_key = "${file("c:/Program Files (x86)/PuTTY/iiotkey.pem")}"
      agent = false
    }
  }


  provisioner "remote-exec" {
    inline = [
      "sudo apt-get update",
      "sudo apt-get install -y openjdk-8-jre",
      "sudo java -cp \"/home/ubuntu/.jar:/home/ubuntu/.jar\" com.lohika.iiot.ServerExample1"
    ]

    connection {
      user = "ubuntu"
      private_key = "${file("c:/Program Files (x86)/PuTTY/iiotkey.pem")}"
      agent = false
    }
  }
}

output "public_hostname" {
  value = "${aws_instance.iiot_poc_demo_1.public_dns}"
}

output "public_ip" {
  value = "${aws_instance.iiot_poc_demo_1.public_ip}"
}

output "private_hostname" {
  value = "${aws_instance.iiot_poc_demo_1.private_dns}"
}

output "private_ip" {
  value = "${aws_instance.iiot_poc_demo_1.private_ip}"
}

resource "aws_security_group" "iiot_poc_demo_sg_1" {
  name = "iiot.poc.demo.security.group.1"

  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port = 22
    to_port = 22
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port = 8443
    to_port = 8443
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  ingress {
    from_port = 8666
    to_port = 8666
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }
}