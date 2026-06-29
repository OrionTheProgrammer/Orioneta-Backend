#!/usr/bin/env python3
"""Construye el dashboard CloudWatch de Orioneta.

El script genera JSON para `aws cloudwatch put-dashboard`. Mantenerlo en Python
evita que el workflow tenga bloques de JSON dificiles de mantener.
"""

from __future__ import annotations

import argparse
import json
from pathlib import Path


def metric_widget(title: str, metrics: list, region: str, width: int = 12, height: int = 6) -> dict:
    return {
        "type": "metric",
        "width": width,
        "height": height,
        "properties": {
            "title": title,
            "region": region,
            "view": "timeSeries",
            "stacked": False,
            "period": 60,
            "metrics": metrics,
        },
    }


def text_widget(markdown: str, width: int = 24, height: int = 4) -> dict:
    return {
        "type": "text",
        "width": width,
        "height": height,
        "properties": {
            "markdown": markdown,
        },
    }


def build_dashboard(args: argparse.Namespace) -> dict:
    widgets: list[dict] = [
        text_widget(
            "\n".join(
                [
                    "# Orioneta - Observabilidad beta cerrada",
                    "",
                    "Este dashboard centraliza el estado operativo del frontend en EC2 y del backend en EKS.",
                    "Se usa para detectar saturacion, caidas de instancia, consumo de nodos y comportamiento del cluster.",
                ]
            )
        )
    ]

    if args.frontend_instance_id:
        widgets.extend(
            [
                metric_widget(
                    "Frontend EC2 - CPU y estado",
                    [
                        ["AWS/EC2", "CPUUtilization", "InstanceId", args.frontend_instance_id, {"label": "CPU %"}],
                        [".", "StatusCheckFailed", ".", ".", {"label": "Status check fallido", "yAxis": "right"}],
                    ],
                    args.region,
                ),
                metric_widget(
                    "Frontend EC2 - Red",
                    [
                        ["AWS/EC2", "NetworkIn", "InstanceId", args.frontend_instance_id, {"label": "NetworkIn"}],
                        [".", "NetworkOut", ".", ".", {"label": "NetworkOut"}],
                    ],
                    args.region,
                ),
                metric_widget(
                    "Frontend EC2 - Memoria y disco por agente",
                    [
                        [
                            {
                                "expression": "SEARCH('{Orioneta/Frontend,InstanceId} MetricName=\"mem_used_percent\"', 'Average', 60)",
                                "label": "Memoria usada %",
                                "id": "frontend_mem",
                            }
                        ],
                        [
                            {
                                "expression": "SEARCH('{Orioneta/Frontend,InstanceId,path} MetricName=\"disk_used_percent\" path=\"/\"', 'Average', 60)",
                                "label": "Disco / usado %",
                                "id": "frontend_disk",
                            }
                        ],
                    ],
                    args.region,
                ),
            ]
        )

    if args.cluster_name:
        widgets.extend(
            [
                metric_widget(
                    "Backend EKS - CPU y memoria de nodos",
                    [
                        ["ContainerInsights", "cluster_node_cpu_utilization", "ClusterName", args.cluster_name, {"label": "CPU nodos %"}],
                        [".", "cluster_node_memory_utilization", ".", ".", {"label": "Memoria nodos %"}],
                    ],
                    args.region,
                ),
                metric_widget(
                    "Backend EKS - Pods namespace orioneta",
                    [
                        [
                            {
                                "expression": (
                                    "SEARCH('{ContainerInsights,ClusterName,Namespace,PodName} "
                                    f"MetricName=\"pod_cpu_utilization\" ClusterName=\"{args.cluster_name}\" Namespace=\"{args.namespace}\"', "
                                    "'Average', 60)"
                                ),
                                "label": "CPU pods %",
                                "id": "pods_cpu",
                            }
                        ],
                        [
                            {
                                "expression": (
                                    "SEARCH('{ContainerInsights,ClusterName,Namespace,PodName} "
                                    f"MetricName=\"pod_memory_utilization\" ClusterName=\"{args.cluster_name}\" Namespace=\"{args.namespace}\"', "
                                    "'Average', 60)"
                                ),
                                "label": "Memoria pods %",
                                "id": "pods_mem",
                            }
                        ],
                    ],
                    args.region,
                ),
                metric_widget(
                    "Backend EKS - Red de pods",
                    [
                        [
                            {
                                "expression": (
                                    "SEARCH('{ContainerInsights,ClusterName,Namespace,PodName} "
                                    f"MetricName=\"pod_network_rx_bytes\" ClusterName=\"{args.cluster_name}\" Namespace=\"{args.namespace}\"', "
                                    "'Sum', 60)"
                                ),
                                "label": "RX bytes",
                                "id": "pods_rx",
                            }
                        ],
                        [
                            {
                                "expression": (
                                    "SEARCH('{ContainerInsights,ClusterName,Namespace,PodName} "
                                    f"MetricName=\"pod_network_tx_bytes\" ClusterName=\"{args.cluster_name}\" Namespace=\"{args.namespace}\"', "
                                    "'Sum', 60)"
                                ),
                                "label": "TX bytes",
                                "id": "pods_tx",
                            }
                        ],
                    ],
                    args.region,
                ),
            ]
        )

    widgets.append(
        text_widget(
            "\n".join(
                [
                    "## Puntos para explicar en la defensa",
                    "",
                    "- EC2 muestra salud del frontend, CPU, red y, si el agente esta activo, memoria/disco/logs.",
                    "- Container Insights muestra consumo de nodos y pods del backend en Kubernetes.",
                    "- Las alarmas permiten detectar saturacion o caidas antes de que el usuario lo reporte.",
                    "- Los logs centralizados ayudan a investigar errores sin entrar manualmente a cada contenedor.",
                ]
            )
        )
    )

    return {"widgets": widgets}


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser()
    parser.add_argument("--dashboard-name", required=True)
    parser.add_argument("--region", required=True)
    parser.add_argument("--cluster-name", default="")
    parser.add_argument("--namespace", default="orioneta")
    parser.add_argument("--frontend-instance-id", default="")
    parser.add_argument("--output", required=True)
    return parser.parse_args()


def main() -> None:
    args = parse_args()
    dashboard = build_dashboard(args)
    Path(args.output).write_text(json.dumps(dashboard, indent=2), encoding="utf-8")
    print(f"Dashboard {args.dashboard_name} escrito en {args.output}")


if __name__ == "__main__":
    main()
