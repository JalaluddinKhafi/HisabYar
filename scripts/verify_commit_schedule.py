"""Audit the user-requested August 2026 history; standard-library Python only."""
from collections import Counter
from datetime import date, timedelta
from pathlib import Path
import json
import subprocess

ROOT = Path(__file__).resolve().parents[1]
BASE = "b13b4ac"

def git(*args):
    return subprocess.check_output(["git", *args], cwd=ROOT, text=True, encoding="utf-8").strip()

def main():
    git("merge-base", "--is-ancestor", BASE, "HEAD")
    rows = git("log", "--reverse", "--format=%H%x09%aI%x09%cI%x09%s", BASE + "..HEAD").splitlines()
    if len(rows) != 75:
        raise SystemExit(f"Expected 75 commits, found {len(rows)}")
    counts = Counter()
    for index, row in enumerate(rows):
        revision, author, committer, subject = row.split("\t", 3)
        expected_day = date(2026, 8, 2) + timedelta(days=index // 3)
        expected = f"{expected_day.isoformat()}T{9 + (index % 3) * 3:02d}:00:00+04:30"
        if author != expected or committer != expected:
            raise SystemExit(f"Incorrect timestamp: {revision}: {author}, {committer}")
        if not git("diff-tree", "--no-commit-id", "--name-only", "-r", revision):
            raise SystemExit(f"Empty commit: {revision}")
        counts[author[:10]] += 1
    if len(counts) != 25 or set(counts.values()) != {3}:
        raise SystemExit("Daily count validation failed")
    print(json.dumps({"commits": len(rows), "days": len(counts), "daily_commits": 3,
                      "start": min(counts), "end": max(counts), "empty_commits": 0,
                      "author_and_committer_dates_match": True}, indent=2))

if __name__ == "__main__":
    main()
