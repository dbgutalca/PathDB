import json

filename = "paths.txt"

with open(filename, "r", encoding="utf-8") as f:
    data = json.load(f)  # Carga todo el JSON como una lista

for idx, path in enumerate(data):
    ids = [obj["id"].lower() for obj in path["sequence"]]
    print(f' '.join(ids), end="\n")