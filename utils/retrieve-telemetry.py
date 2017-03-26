'''
Requires Python 3. Run the following commands to install dependencies:

pip install pypiwin32
pip install xlwings
pip install paramiko
'''

import xlwings as excel
import paramiko
import os
import argparse
import glob

remote_data_folder = "/home/lvuser/488-telemetry/"

parser = argparse.ArgumentParser(description='Retrieve and chart telemetry data.')
parser.add_argument('target',
                    help='The IP address or DNS name of the target to connect to')
parser.add_argument('--local',
                    help='Use files in the given local directory (for testing)',
                    dest='local', action='store_true')

args = parser.parse_args()

book = excel.Book()
if args.local:
    files = glob.glob(args.target)
else:
    ssh_client = paramiko.SSHClient()
    ssh_client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    ssh_client.connect(args.target, username='admin', password='')

    sftp_client = ssh_client.open_sftp()
    files = sftp_client.listdir(remote_data_folder)

for file_name in files:
    if args.local:
        remote_file = open(file_name, 'r')
    else:
        print(file_name)
        remote_file = sftp_client.open(remote_data_folder + file_name)

    try:
        source_data = [line.rstrip().split(",") for line in remote_file]
    finally:
        remote_file.close()
    
    chartable_data = source_data #[list(v) for v in zip(*source_data)]

    sheet_name = os.path.splitext(os.path.basename(file_name))[0]
    book.sheets.add(sheet_name)

    book.sheets[sheet_name].range("A1").value = chartable_data
    chart = excel.Chart()
    chart.chart_type = 'xy_scatter_lines'
    chart.top = 100
    chart.left = 300
    chart.set_source_data(book.sheets[sheet_name].range("A1").expand())